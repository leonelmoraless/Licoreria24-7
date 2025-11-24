/*
 * Configurancion de las operaciones de compras con el patron de diseño facade,
 permite registrar nueva compra, mostrar las compras y ver el historial y eliminar compras
 */
package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.patrones.facade.Facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControladorCompra {

    @Autowired
    private Facade facade;

    // Metodo para registrar una nueva compra 
    @GetMapping("/compras")
    public String mostrarPaginaRegistroCompras(Model model) {

        model.addAttribute("listaProveedores", facade.obtenerTodosProveedores());
        model.addAttribute("listaProductos", facade.obtenerTodosProductos());
        return "compras";
    }

    
    // Muestra el historial de todas las compras registradas
     
    @GetMapping("/historial-compras")
    public String mostrarPaginaHistorialCompras(Model model) {
        model.addAttribute("listaCompras", facade.obtenerTodasLasCompras());
        return "historial-compras"; 
    }

    
    //Api para registrar compras desde un formato JSON que devuelve 
    // la compra guardada o algun error inesperado
     
    @PostMapping("/api/compras/registrar")
    @ResponseBody 
    public ResponseEntity<?> registrarCompraAPI(@RequestBody CompraPeticionDTO peticion) {
        try {
            Compra compraGuardada = facade.registrarCompra(peticion);

            return ResponseEntity.ok(compraGuardada);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    // Metodo para eliminar una compra del historial usando el id
     
    @PostMapping("/compras/eliminar")
    public String eliminarCompra(@RequestParam("id") Long idCompra, Model model) {
        try {
            facade.eliminarCompra(idCompra);
            return "redirect:/historial-compras";

        } catch (Exception e) {
            return "redirect:/historial-compras?error=" + e.getMessage();
        }
    }
}