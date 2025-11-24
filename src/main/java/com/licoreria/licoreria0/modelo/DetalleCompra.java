/*
 * detalla la compra realizada, incluyendo cantidad, precio unitario, subtotal,
 * y las relaciones con la compra y el producto asociado
 */
package com.licoreria.licoreria0.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_compras")
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_compra_pk")
    private Long idDetalleCompra;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    // Muchos detalles pertenecen a Una compra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compra_fk", nullable = false)
    private Compra compra;

    // Muchos detalles refieren a Un producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto_fk", nullable = false)
    private Producto producto;


    public DetalleCompra() {
    }

    public DetalleCompra(Integer cantidad, Double precioUnitario, Double subtotal, Compra compra, Producto producto) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.compra = compra;
        this.producto = producto;
    }

    public Long getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}