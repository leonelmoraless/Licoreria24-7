package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Pago;
import com.licoreria.licoreria0.modelo.Venta;
import com.licoreria.licoreria0.modelo.VentaPeticionDTO;
import com.licoreria.licoreria0.modelo.dto.VentaResumenDTO;
import com.licoreria.licoreria0.patrones.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorVenta {

    @Autowired
    private Facade facade;

    @GetMapping("/ventas")
    public String mostrarPaginaRegistroVentas(Model model) {
        model.addAttribute("listaClientes", facade.obtenerTodosClientes());
        model.addAttribute("listaProductos", facade.obtenerTodosProductos());
        return "ventas";
    }

    @GetMapping("/historial-ventas")
    public String mostrarPaginaHistorialVentas(Model model) {
        model.addAttribute("listaVentas", facade.obtenerTodasVentas());
        return "historial-ventas";
    }

    @PostMapping("/api/ventas/registrar")
    @ResponseBody
    public ResponseEntity<?> registrarVentaAPI(@RequestBody VentaPeticionDTO peticion) {
        try {
            // 1. El Facade hace todo el trabajo pesado (Builder, Strategy, Observer)
            Venta ventaGuardada = facade.registrarVenta(peticion);

            // 2. CONVERSIÓN MANUAL (Mapeo) Entidad -> DTO usando método helper
            VentaResumenDTO resumen = convertirADTO(ventaGuardada);

            // 3. Devolvemos el DTO con código 200 OK
            return ResponseEntity.ok(resumen);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint nuevo para obtener los detalles de una venta específica (ticket)
    @GetMapping("/api/ventas/{id}")
    @ResponseBody
    public ResponseEntity<?> obtenerDetalleVentaAPI(@PathVariable("id") Long idVenta) {
        try {
            Venta venta = facade.obtenerTodasVentas().stream()
                    .filter(v -> v.getIdVenta().equals(idVenta))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Venta no encontrada"));

            VentaResumenDTO resumen = convertirADTO(venta);

            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ventas/eliminar")
    public String eliminarVenta(@RequestParam("id") Long idVenta, RedirectAttributes attributes) {
        try {
            facade.eliminarVenta(idVenta);
            attributes.addFlashAttribute("mensajeExito", "Venta eliminada y stock restaurado.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/historial-ventas";
    }

    // Endpoint para servir comprobantes de transferencia
    @GetMapping("/uploads/comprobantes/{filename:.+}")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> servirArchivo(@PathVariable String filename) {
        try {
            java.nio.file.Path file = java.nio.file.Paths.get("uploads/comprobantes").resolve(filename);
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Método helper para convertir Venta a DTO
    private VentaResumenDTO convertirADTO(Venta venta) {
        List<VentaResumenDTO.ItemResumen> itemsDTO = new ArrayList<>();
        double totalDescuento = 0.0;

        for (com.licoreria.licoreria0.modelo.DetalleVenta detalle : venta.getDetalles()) {
            double precioTotalSinDescuento = detalle.getCantidad() * detalle.getPrecio();
            double descuentoMonto = precioTotalSinDescuento * (detalle.getDescuento() / 100.0);
            totalDescuento += descuentoMonto;

            itemsDTO.add(new VentaResumenDTO.ItemResumen(
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getPrecio(),
                    detalle.getDescuento(),
                    detalle.getSubtotalConDescuento()));
        }

        // Obtener datos de pago (asumimos un solo pago principal)
        String metodoPago = "Desconocido";
        String numeroTransferencia = null;
        String rutaComprobante = null;

        if (venta.getPagos() != null && !venta.getPagos().isEmpty()) {
            Pago pago = venta.getPagos().get(0);
            metodoPago = pago.getMetodoPago();
            numeroTransferencia = pago.getNumeroTransferencia();
            rutaComprobante = pago.getRutaComprobante();

            // Convertir ruta absoluta a URL relativa para el frontend
            if (rutaComprobante != null && !rutaComprobante.isEmpty()) {
                java.io.File f = new java.io.File(rutaComprobante);
                rutaComprobante = "/uploads/comprobantes/" + f.getName();
            }
        }

        return new VentaResumenDTO(
                venta.getIdVenta(),
                venta.getCliente().getNombre(),
                venta.getFecha().toString(),
                venta.getTotal(),
                venta.getSubtotal(),
                venta.getMontoIva(),
                totalDescuento,
                metodoPago,
                numeroTransferencia,
                rutaComprobante,
                itemsDTO);
    }
}
