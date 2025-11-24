/*
 * Controla las acciones relacionadas con los usuarios, lista, registra, actualiza y elimina usuarios.
 */
package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Usuario;
import com.licoreria.licoreria0.patrones.facade.Facade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    private Facade facade; 

    
    //Muestra la pagina principal de los usuarios
    
    @GetMapping
    public String mostrarPaginaUsuarios(Model model) {
        model.addAttribute("listaUsuarios", facade.obtenerTodosLosUsuarios());

        
        if (!model.containsAttribute("nuevoUsuario")) {
            model.addAttribute("nuevoUsuario", new Usuario());
        }
        if (!model.containsAttribute("usuarioEditar")) {
            model.addAttribute("usuarioEditar", new Usuario());
        }

        return "usuario";
    }

    
    //Metodo para registrar un nuevo usuario
    
    @PostMapping("/registrar")
    public String registrarUsuario(@ModelAttribute("nuevoUsuario") Usuario usuario, RedirectAttributes attributes) {
        try {
            facade.registrarUsuario(usuario);
            attributes.addFlashAttribute("mensajeExito", "Usuario registrado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al registrar: " + e.getMessage());
            
            attributes.addFlashAttribute("nuevoUsuario", usuario);
        }
        return "redirect:/usuario";
    }

    
    //Procesa la actualizacion de un usuario registrado
    
    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute("usuarioEditar") Usuario usuario, RedirectAttributes attributes) {
        try {
            facade.actualizarUsuario(usuario);
            attributes.addFlashAttribute("mensajeExito", "Usuario actualizado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
            attributes.addFlashAttribute("usuarioEditar", usuario);
        }
        return "redirect:/usuario";
    }

    
    //Metodo para eliminar un usuario usando el id
    
    @PostMapping("/eliminar")
    public String eliminarUsuario(@ModelAttribute("id") Long idUsuario, RedirectAttributes attributes) {
        try {
            facade.eliminarUsuario(idUsuario);
            attributes.addFlashAttribute("mensajeExito", "Usuario eliminado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/usuario";
    }
}