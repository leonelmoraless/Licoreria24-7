package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Cliente;
import com.licoreria.licoreria0.patrones.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ControladorCliente {

    @Autowired
    private Facade facade;

    @GetMapping
    public String mostrarPaginaClientes(Model model) {
        model.addAttribute("listaClientes", facade.obtenerTodosClientes());
        model.addAttribute("nuevoCliente", new Cliente());
        model.addAttribute("clienteEditar", new Cliente());
        return "clientes";
    }

    @PostMapping("/registrar")
    public String registrarCliente(@ModelAttribute("nuevoCliente") Cliente cliente,
            RedirectAttributes attributes) {
        try {
            facade.registrarCliente(cliente);
            attributes.addFlashAttribute("mensajeExito", "Cliente registrado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al registrar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute("clienteEditar") Cliente cliente,
            RedirectAttributes attributes) {
        try {
            facade.actualizarCliente(cliente);
            attributes.addFlashAttribute("mensajeExito", "Cliente actualizado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @PostMapping("/eliminar")
    public String eliminarCliente(@RequestParam("id") Long idCliente, RedirectAttributes attributes) {
        try {
            facade.eliminarCliente(idCliente);
            attributes.addFlashAttribute("mensajeExito", "Cliente eliminado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}
