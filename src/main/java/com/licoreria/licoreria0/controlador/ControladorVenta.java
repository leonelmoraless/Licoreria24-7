package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Venta;
import com.licoreria.licoreria0.modelo.VentaPeticionDTO;
import com.licoreria.licoreria0.patrones.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

            // 2. CONVERSIÓN MANUAL (Mapeo) Entidad -> DTO
            java.util.List<com.licoreria.licoreria0.modelo.dto.VentaResumenDTO.ItemResumen> itemsDTO = new java.util.ArrayList<>();

            for (com.licoreria.licoreria0.modelo.DetalleVenta detalle : ventaGuardada.getDetalles()) {
                itemsDTO.add(new com.licoreria.licoreria0.modelo.dto.VentaResumenDTO.ItemResumen(
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecio(),
                        detalle.getCantidad() * detalle.getPrecio()));
            }

            com.licoreria.licoreria0.modelo.dto.VentaResumenDTO resumen = new com.licoreria.licoreria0.modelo.dto.VentaResumenDTO(
                    ventaGuardada.getIdVenta(),
                    ventaGuardada.getCliente().getNombre(),
                    ventaGuardada.getFecha().toString(),
                    ventaGuardada.getTotal(),
                    itemsDTO);

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

            // Reusamos la lógica de DTO (podríamos extraer esto a un método utilitario o al
            // Facade)
            java.util.List<com.licoreria.licoreria0.modelo.dto.VentaResumenDTO.ItemResumen> itemsDTO = new java.util.ArrayList<>();
            for (com.licoreria.licoreria0.modelo.DetalleVenta detalle : venta.getDetalles()) {
                itemsDTO.add(new com.licoreria.licoreria0.modelo.dto.VentaResumenDTO.ItemResumen(
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecio(),
                        detalle.getCantidad() * detalle.getPrecio()));
            }

            com.licoreria.licoreria0.modelo.dto.VentaResumenDTO resumen = new com.licoreria.licoreria0.modelo.dto.VentaResumenDTO(
                    venta.getIdVenta(),
                    venta.getCliente().getNombre(),
                    venta.getFecha().toString(),
                    venta.getTotal(),
                    itemsDTO);

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
}
