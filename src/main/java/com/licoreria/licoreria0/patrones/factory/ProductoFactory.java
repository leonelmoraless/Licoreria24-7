
// Esta es la que crea los objetos, centraliza las reglas de validación y 
// construcción de un Producto nuevo
package com.licoreria.licoreria0.patrones.factory;

import com.licoreria.licoreria0.modelo.Producto;
import com.licoreria.licoreria0.modelo.Proveedor;
import org.springframework.stereotype.Component;

@Component
public class ProductoFactory {

    // metodo para crear un producto nuevo con sus validaciones
    public Producto crearProducto(String nombre, Double precioCompra, Double precioVenta, Integer stock,
            Proveedor proveedor)
            throws Exception {

        // valida el nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del producto no puede ser nulo o vacío.");
        }
        // valida los precios
        if (precioCompra == null || precioCompra <= 0) {
            throw new Exception("El precio de compra debe ser positivo.");
        }
        if (precioVenta == null || precioVenta <= 0) {
            throw new Exception("El precio de venta debe ser positivo.");
        }
        if (precioVenta < precioCompra) {
            throw new Exception("El precio de venta no puede ser menor que el precio de compra.");
        }

        // valida el stock
        if (stock == null || stock < 0) {
            throw new Exception("El stock no puede ser negativo.");
        }
        // valida el proveedor
        if (proveedor == null) {
            throw new Exception("El producto debe tener un proveedor asociado.");
        }

        // Despues de pasar se crea el producto
        Producto producto = new Producto();
        producto.setNombre(nombre.trim());
        producto.setPrecioCompra(precioCompra);
        producto.setPrecioVenta(precioVenta);
        producto.setStock(stock);
        producto.setProveedor(proveedor);

        System.out.println("ProductoFactory: Creando producto '" + nombre + "' con éxito.");
        return producto;
    }
}