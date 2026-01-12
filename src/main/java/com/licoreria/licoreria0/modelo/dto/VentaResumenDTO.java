package com.licoreria.licoreria0.modelo.dto;

import java.util.List;

// Una clase simple "POJO" (Plain Old Java Object)
public class VentaResumenDTO {

    private Long idVenta;
    private String nombreCliente;
    private String fecha;
    private Double subtotal;
    private Double totalDescuento;
    private Double montoIva;
    private Double total;
    private String metodoPago;

    // Transferencia
    private String numeroTransferencia;
    private String rutaComprobante;

    private List<ItemResumen> items;

    public VentaResumenDTO() {
    }

    public VentaResumenDTO(Long idVenta, String nombreCliente, String fecha, Double total,
            String metodoPago, String numeroTransferencia, String rutaComprobante,
            Double subtotal, Double totalDescuento, Double montoIva, List<ItemResumen> items) {
        this.idVenta = idVenta;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
        this.numeroTransferencia = numeroTransferencia;
        this.rutaComprobante = rutaComprobante;
        this.subtotal = subtotal;
        this.totalDescuento = totalDescuento;
        this.montoIva = montoIva;
        this.items = items;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroTransferencia() {
        return numeroTransferencia;
    }

    public void setNumeroTransferencia(String numeroTransferencia) {
        this.numeroTransferencia = numeroTransferencia;
    }

    public String getRutaComprobante() {
        return rutaComprobante;
    }

    public void setRutaComprobante(String rutaComprobante) {
        this.rutaComprobante = rutaComprobante;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(Double totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public Double getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(Double montoIva) {
        this.montoIva = montoIva;
    }

    public List<ItemResumen> getItems() {
        return items;
    }

    public void setItems(List<ItemResumen> items) {
        this.items = items;
    }

    public static class ItemResumen {
        private String producto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
        private Double descuento;

        public ItemResumen() {
        }

        public ItemResumen(String producto, Integer cantidad, Double precioUnitario, Double subtotal,
                Double descuento) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
            this.descuento = descuento;
        }

        public String getProducto() {
            return producto;
        }

        public void setProducto(String producto) {
            this.producto = producto;
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

        public Double getDescuento() {
            return descuento;
        }

        public void setDescuento(Double descuento) {
            this.descuento = descuento;
        }
    }
}
