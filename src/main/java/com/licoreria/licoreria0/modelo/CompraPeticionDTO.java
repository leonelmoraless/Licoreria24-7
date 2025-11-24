/*
 * sirve para transportar los datos que se envia desde el javaScript
 */
package com.licoreria.licoreria0.modelo;

import java.util.List;

public class CompraPeticionDTO {

    private Long idProveedor;
    private List<ItemCompraDTO> items;

    // Constructor vacio
    public CompraPeticionDTO() {
    }

    // getters y setters
    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public List<ItemCompraDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemCompraDTO> items) {
        this.items = items;
    }

    // clase interna para representar cada item de la compra con sus atributos
    public static class ItemCompraDTO {
        private Long idProducto;
        private Integer cantidad;
        private Double precioUnitario;

        public ItemCompraDTO() {
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
    }
}