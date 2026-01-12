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
    private double subtotalAcumulado;
    private Double ivaPorcentaje = 15.0;

    public VentaBuilder() {
        this.venta = new Venta();
        this.venta.setDetalles(new ArrayList<>());
        this.venta.setFecha(new Date());
        this.subtotalAcumulado = 0.0;
    }

    public VentaBuilder conCliente(Cliente cliente) {
        this.venta.setCliente(cliente);
        return this;
    }

    public VentaBuilder agregarDetalle(Producto producto, Integer cantidad, Double precioUnitario, Double descuento)
            throws Exception {
        // Validacion rapida de stock
        if (producto.getStock() < cantidad) {
            throw new Exception("Stock insuficiente para: " + producto.getNombre());
        }

        double subtotal = precioUnitario * cantidad;
        this.subtotalAcumulado += subtotal;

        // Crear el detalle con descuento
        DetalleVenta detalle = new DetalleVenta(this.venta, producto, cantidad, precioUnitario, descuento);
        this.venta.getDetalles().add(detalle);

        return this;
    }

    public Venta construir() throws Exception {
        if (this.venta.getCliente() == null) {
            throw new Exception("No se puede crear una venta sin cliente.");
        }

        // Total simple sin IVA ni descuentos
        this.venta.setTotal(this.subtotalAcumulado);
        return this.venta;
    }
}
