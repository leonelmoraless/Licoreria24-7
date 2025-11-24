/*
 * Controla las acciones relacionadas con los proveedores, permite listar, registrar, actualizar y eliminar proveedores.
 */
package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Proveedor;
import com.licoreria.licoreria0.patrones.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
public class ControladorProveedor {

    @Autowired
    private Facade facade;

    
    //Muestra la pagina principal de los proveedores, la Lista de proveedores registrados y editar proveedores
    
    @GetMapping
    public String mostrarPaginaProveedores(Model model) {
        model.addAttribute("listaProveedores", facade.obtenerTodosProveedores());

       
        model.addAttribute("nuevoProveedor", new Proveedor());
        model.addAttribute("proveedorEditar", new Proveedor());

        return "proveedores"; 
    }

    
    //Metodo para registrar un nuevo proveedor usando el facade para validar y guardar la informacion
    
    @PostMapping("/registrar")
    public String registrarProveedor(@ModelAttribute("nuevoProveedor") Proveedor proveedor,
            RedirectAttributes attributes) {
        try {
            facade.registrarProveedor(proveedor);
            attributes.addFlashAttribute("mensajeExito", "Proveedor registrado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al registrar: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    
    //Metodo para actualizar un proveedor existente
    
    @PostMapping("/actualizar")
    public String actualizarProveedor(@ModelAttribute("proveedorEditar") Proveedor proveedor,
            RedirectAttributes attributes) {
        try {
            facade.actualizarProveedor(proveedor);
            attributes.addFlashAttribute("mensajeExito", "Proveedor actualizado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }

    
    //Metodo para eliminar un proveedor usando el id
    
    @PostMapping("/eliminar")
    public String eliminarProveedor(@ModelAttribute("id") Long idProveedor, RedirectAttributes attributes) {
        try {
            facade.eliminarProveedor(idProveedor);
            attributes.addFlashAttribute("mensajeExito", "Proveedor eliminado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }
}