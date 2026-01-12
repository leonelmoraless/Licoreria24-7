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

    public VentaBuilder conIva(Double iva) {
        this.ivaPorcentaje = iva != null ? iva : 15.0;
        return this;
    }

    public VentaBuilder agregarDetalle(Producto producto, Integer cantidad, Double precioUnitario) throws Exception {
        return agregarDetalle(producto, cantidad, precioUnitario, 0.0);
    }

    public VentaBuilder agregarDetalle(Producto producto, Integer cantidad, Double precioUnitario, Double descuento)
            throws Exception {

        // Validacion rapida de stock en el builder (Fail Fast)
        if (producto.getStock() < cantidad) {
            throw new Exception("Stock insuficiente para: " + producto.getNombre());
        }

        double descuentoPorcentaje = descuento != null ? descuento : 0.0;

        // Calcular subtotal con descuento aplicado
        double subtotalSinDescuento = precioUnitario * cantidad;
        double montoDescuento = subtotalSinDescuento * (descuentoPorcentaje / 100.0);
        double subtotalConDescuento = subtotalSinDescuento - montoDescuento;

        this.subtotalAcumulado += subtotalConDescuento;

        // Crear el detalle con descuento
        DetalleVenta detalle = new DetalleVenta(this.venta, producto, cantidad, precioUnitario, descuentoPorcentaje);

        // Agregar a la lista de detalles de la venta
        this.venta.getDetalles().add(detalle);

        return this;
    }

    public Venta construir() throws Exception {
        if (this.venta.getCliente() == null) {
            throw new Exception("No se puede crear una venta sin cliente.");
        }

        // Calcular IVA y total
        double montoIva = this.subtotalAcumulado * (this.ivaPorcentaje / 100.0);
        double totalFinal = this.subtotalAcumulado + montoIva;

        this.venta.setSubtotal(this.subtotalAcumulado);
        this.venta.setIva(this.ivaPorcentaje);
        this.venta.setMontoIva(montoIva);
        this.venta.setTotal(totalFinal);

        return this.venta;
    }
}
