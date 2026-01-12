package com.licoreria.licoreria0.modelo;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta_pk")
    private Long idVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente_fk")
    private Cliente cliente;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "iva")
    private Double iva = 15.0;

    @Column(name = "monto_iva")
    private Double montoIva = 0.0;

    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    public Venta() {
    }

    public Venta(Cliente cliente, Date fecha, Double total) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIva() {
        return iva != null ? iva : 15.0;
    }

    public void setIva(Double iva) {
        this.iva = iva != null ? iva : 15.0;
    }

    public Double getMontoIva() {
        return montoIva != null ? montoIva : 0.0;
    }

    public void setMontoIva(Double montoIva) {
        this.montoIva = montoIva != null ? montoIva : 0.0;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
