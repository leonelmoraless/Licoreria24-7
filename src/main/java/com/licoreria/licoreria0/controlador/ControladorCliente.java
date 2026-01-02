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
        // model.addAttribute("clienteEditar", new Cliente()); // Uncomment when update
        // is implemented
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
}
