package com.licoreria.licoreria0.modelo;

import java.util.List;

public class VentaPeticionDTO {

    private Long idCliente;
    private String metodoPago;
    private List<ItemVentaDTO> items;
    private Double iva = 15.0;
    private String numeroTransferencia;
    private String comprobanteBase64;

    public VentaPeticionDTO() {
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<ItemVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemVentaDTO> items) {
        this.items = items;
    }

    public Double getIva() {
        return iva != null ? iva : 15.0;
    }

    public void setIva(Double iva) {
        this.iva = iva != null ? iva : 15.0;
    }

    public String getNumeroTransferencia() {
        return numeroTransferencia;
    }

    public void setNumeroTransferencia(String numeroTransferencia) {
        this.numeroTransferencia = numeroTransferencia;
    }

    public String getComprobanteBase64() {
        return comprobanteBase64;
    }

    public void setComprobanteBase64(String comprobanteBase64) {
        this.comprobanteBase64 = comprobanteBase64;
    }

    public static class ItemVentaDTO {
        private Long idProducto;
        private Integer cantidad;
        private Double precioUnitario;
        private Double descuento = 0.0;

        public ItemVentaDTO() {
        }

        public Long getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(Long idProducto) {
            this.idProducto = idProducto;
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
            return descuento != null ? descuento : 0.0;
        }

        public void setDescuento(Double descuento) {
            this.descuento = descuento != null ? descuento : 0.0;
        }
    }
}
