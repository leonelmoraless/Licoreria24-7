/*
 * controlador para gestionar las operaciones relacionadas con los productos, 
como listar, registrar, actualizar y eliminar productos.
 */
package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.*;

import com.licoreria.licoreria0.patrones.facade.Facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ControladorProducto {

    @Autowired
    private Facade facade;

    // Muestra la página principal de productos

    @GetMapping
    public String mostrarPaginaProductos(Model model) {

        model.addAttribute("listaProductos", facade.obtenerTodosProductos());

        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("productoEditar", new Producto());

        model.addAttribute("listaProveedores", facade.obtenerTodosProveedores());

        return "productos";
    }

    // Procesa el registro de un nuevo producto con el uso de facade
    // para validar y guardar la informacion

    @PostMapping("/registrar")
    public String registrarProducto(@ModelAttribute("nuevoProducto") Producto producto,
            @RequestParam("idProveedor") Long idProveedor,
            RedirectAttributes attributes) {
        System.out.println("Registrando producto: " + producto.getNombre());
        System.out.println("Precio Compra: " + producto.getPrecioCompra());
        System.out.println("Precio Venta: " + producto.getPrecioVenta());
        try {

            facade.registrarProducto(producto, idProveedor);

            attributes.addFlashAttribute("mensajeExito", "Producto registrado exitosamente.");
        } catch (Exception e) {

            attributes.addFlashAttribute("mensajeError", "Error al registrar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    // Metodo para editar un producto registrado, guarda y valida los cambios usando
    // el facade

    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute("productoEditar") Producto producto,
            @RequestParam("idProveedorEdit") Long idProveedor,
            RedirectAttributes attributes) {
        System.out.println("Actualizando producto ID: " + producto.getIdProducto());
        System.out.println("Precio Venta Recibido: " + producto.getPrecioVenta());
        try {
            facade.actualizarProducto(producto, idProveedor);
            attributes.addFlashAttribute("mensajeExito", "Producto actualizado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    // Metodo para eliminar un producto usando el id

    @PostMapping("/eliminar")
    public String eliminarProducto(@ModelAttribute("id") Long idProducto, RedirectAttributes attributes) {
        try {
            facade.eliminarProducto(idProducto);
            attributes.addFlashAttribute("mensajeExito", "Producto eliminado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}