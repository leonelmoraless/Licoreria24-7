package com.licoreria.licoreria0.patrones.builder;

import com.licoreria.licoreria0.modelo.Cliente;
import com.licoreria.licoreria0.modelo.DetalleVenta;
import com.licoreria.licoreria0.modelo.Producto;
import com.licoreria.licoreria0.modelo.Venta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaBuilder {

    private Venta venta;
    private double totalAcumulado;

    public VentaBuilder() {
        this.venta = new Venta();
        this.venta.setDetalles(new ArrayList<>()); // Inicializamos la lista para evitar NullPointer
        this.venta.setFecha(new Date()); // Fecha por defecto ahora
        this.totalAcumulado = 0.0;
    }

    public VentaBuilder conCliente(Cliente cliente) {
        this.venta.setCliente(cliente);
        return this;
    }

    public VentaBuilder agregarDetalle(Producto producto, Integer cantidad, Double precioUnitario) throws Exception {

        // Validacion rapida de stock en el builder (Fail Fast)
        if (producto.getStock() < cantidad) {
            throw new Exception("Stock insuficiente para: " + producto.getNombre());
        }

        double subtotal = precioUnitario * cantidad;
        this.totalAcumulado += subtotal;

        // Crear el detalle y asociarlo
        DetalleVenta detalle = new DetalleVenta(this.venta, producto, cantidad, precioUnitario);

        // Agregar a la lista de detalles de la venta
        this.venta.getDetalles().add(detalle);

        return this;
    }

    public Venta construir() throws Exception {
        if (this.venta.getCliente() == null) {
            throw new Exception("No se puede crear una venta sin cliente.");
        }
        this.venta.setTotal(this.totalAcumulado);
        return this.venta;
    }
}
