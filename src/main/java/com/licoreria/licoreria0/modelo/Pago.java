package com.licoreria.licoreria0.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_pk")
    private Long idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta_fk")
    private Venta venta;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    public Pago() {
    }

    public Pago(Venta venta, String metodoPago) {
        this.venta = venta;
        this.metodoPago = metodoPago;
    }

    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
