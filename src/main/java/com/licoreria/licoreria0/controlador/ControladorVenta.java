package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Venta;
import com.licoreria.licoreria0.modelo.VentaPeticionDTO;
import com.licoreria.licoreria0.patrones.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            Venta ventaGuardada = facade.registrarVenta(peticion);
            return ResponseEntity.ok(ventaGuardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
