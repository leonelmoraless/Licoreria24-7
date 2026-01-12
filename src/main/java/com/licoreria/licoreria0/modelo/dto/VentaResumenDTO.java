package com.licoreria.licoreria0.modelo.dto;

import java.util.List;

// Una clase simple "POJO" (Plain Old Java Object)
public class VentaResumenDTO {

    private Long idVenta;
    private String nombreCliente;
    private String fecha; // Lo manejaremos como String para facilitar visualización
    private Double total;
    private Double subtotal;
    private Double montoIva;
    private Double totalDescuento;
    private String metodoPago;
    private String numeroTransferencia;
    private String rutaComprobante;

    private List<ItemResumen> items; // Lista de productos simplificada

    // Constructor vacío
    public VentaResumenDTO() {
    }

    // Constructor completo
    public VentaResumenDTO(Long idVenta, String nombreCliente, String fecha, Double total,
            Double subtotal, Double montoIva, Double totalDescuento,
            String metodoPago, String numeroTransferencia, String rutaComprobante,
            List<ItemResumen> items) {
        this.idVenta = idVenta;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.total = total;
        this.subtotal = subtotal;
        this.montoIva = montoIva;
        this.totalDescuento = totalDescuento;
        this.metodoPago = metodoPago;
        this.numeroTransferencia = numeroTransferencia;
        this.rutaComprobante = rutaComprobante;
        this.items = items;
    }

    // Getters y Setters
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(Double montoIva) {
        this.montoIva = montoIva;
    }

    public Double getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(Double totalDescuento) {
        this.totalDescuento = totalDescuento;
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

    public List<ItemResumen> getItems() {
        return items;
    }

    public void setItems(List<ItemResumen> items) {
        this.items = items;
    }

    // Clase interna estática para representar cada línea del ticket
    public static class ItemResumen {
        private String producto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double descuento;
        private Double subtotal;

        public ItemResumen() {
        }

        public ItemResumen(String producto, Integer cantidad, Double precioUnitario, Double descuento,
                Double subtotal) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.descuento = descuento;
            this.subtotal = subtotal;
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

        public Double getDescuento() {
            return descuento;
        }

        public void setDescuento(Double descuento) {
            this.descuento = descuento;
        }

        public Double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(Double subtotal) {
            this.subtotal = subtotal;
        }
    }
}
