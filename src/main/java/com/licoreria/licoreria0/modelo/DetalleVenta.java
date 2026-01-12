package com.licoreria.licoreria0.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_ventas")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pk")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta_fk")
    private Venta venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto_fk")
    private Producto producto;

    private Integer cantidad;

    private Double precio;

    @Column(name = "descuento")
    private Double descuento = 0.0;

    public DetalleVenta() {
    }

    public DetalleVenta(Venta venta, Producto producto, Integer cantidad, Double precio) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = 0.0;
    }

    public DetalleVenta(Venta venta, Producto producto, Integer cantidad, Double precio, Double descuento) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento != null ? descuento : 0.0;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getDescuento() {
        return descuento != null ? descuento : 0.0;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento != null ? descuento : 0.0;
    }

    /**
     * Calcula el subtotal del detalle aplicando el descuento
     */
    public Double getSubtotalConDescuento() {
        double subtotal = cantidad * precio;
        double descuentoAplicado = subtotal * (getDescuento() / 100.0);
        return subtotal - descuentoAplicado;
    }
}
