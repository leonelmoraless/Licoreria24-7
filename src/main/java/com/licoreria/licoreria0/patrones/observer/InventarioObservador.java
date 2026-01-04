package com.licoreria.licoreria0.patrones.observer;

import com.licoreria.licoreria0.modelo.DetalleVenta;
import com.licoreria.licoreria0.modelo.Producto;
import com.licoreria.licoreria0.modelo.Venta;
import com.licoreria.licoreria0.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Observador concreto que actualiza el stock cuando se confirma una venta
@Component
public class InventarioObservador implements ObservadorVenta {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    public void notificarVenta(Venta venta) throws Exception {
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            Integer cantidadVendida = detalle.getCantidad();

            // Refrescamos el producto desde la base de datos para tener el stock mas actual
            Producto productoActualizado = productoRepositorio.findById(producto.getIdProducto())
                    .orElseThrow(() -> new Exception("Producto no encontrado durante la actualización de inventario"));

            int stockActual = productoActualizado.getStock();
            int nuevoStock = stockActual - cantidadVendida;

            // Validacion de seguridad (aunque ya deberia haberse validado antes)
            if (nuevoStock < 0) {
                throw new Exception("Error Crítico de Integridad: Stock negativo detectado en producto ID "
                        + producto.getIdProducto());
            }

            productoActualizado.setStock(nuevoStock);
            productoRepositorio.save(productoActualizado);

            System.out.println("InventarioObservador: Stock actualizado para producto "
                    + productoActualizado.getNombre() + ". Nuevo stock: " + nuevoStock);
        }
    }
}
