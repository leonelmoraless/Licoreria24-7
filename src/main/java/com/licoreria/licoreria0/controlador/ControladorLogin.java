/*
 * Controla el inicio de sesión, cierre de sesión y recuperación de contraseña
 */
package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.Usuario;
import com.licoreria.licoreria0.patrones.facade.Facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ControladorLogin {

    @Autowired
    private Facade facade;

    // Muestra la página de login

    @GetMapping("/login")
    public String mostrarPaginaLogin(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("mensajeError", "Usuario o contraseña incorrectos.");
        }

        if (logout != null) {
            model.addAttribute("mensajeLogout", "Has cerrado sesión exitosamente.");
        }
        return "login";
    }

    // Muestra la página de inicio después de iniciar sesion

    @GetMapping({ "/", "/home" })
    public String mostrarPaginaHome(Model model, Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = facade.buscarUsuarioParaLogin(correo).get();
        String nombre = usuario.getNombre();
        model.addAttribute("nombreUsuario", "Bienvenido, " + nombre + "!");
        return "home";
    }

    // Muestra la página para recuperar contraseña

    @GetMapping("/recuperar-contrasena")
    public String mostrarPaginaRecuperar() {
        return "recuperar";
    }

    // Metodo de recuperación de contraseña enviando una solicitud al correo del
    // usuario

    @PostMapping("/recuperar-contrasena")
    public String procesarRecuperacion(@RequestParam("correo") String correo, RedirectAttributes attributes) {

        facade.recuperarContrasena(correo);

        attributes.addFlashAttribute("mensajeExito",
                "Si el correo existe en nuestro sistema, se ha enviado una nueva contraseña.");
        return "redirect:/login";
    }

    // Metodo del cambio de contraseña del usuario autenticado

    @PostMapping("/cambiar-contrasena")
    public String procesarCambioContrasena(@RequestParam("contrasenaActual") String cActual,
            @RequestParam("nuevaContrasena") String cNueva,
            @RequestParam("confirmarContrasena") String cConf,
            RedirectAttributes attributes) {
        try {
            facade.cambiarContrasena(cActual, cNueva, cConf);
            attributes.addFlashAttribute("mensajeExito", "Contraseña cambiada exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/home";
    }

    // Muestra página de error personalizada

    @GetMapping("/error")
    public String mostrarPaginaError() {
        return "error";
    }
}