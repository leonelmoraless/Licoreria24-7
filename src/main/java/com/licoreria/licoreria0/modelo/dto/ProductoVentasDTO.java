package com.licoreria.licoreria0.modelo.dto;

/**
 * DTO para representar los productos más vendidos con información agregada
 */
public class ProductoVentasDTO {

    private Long idProducto;
    private String nombreProducto;
    private Long cantidadVendida;
    private Double totalVentas;

    // Constructor vacío
    public ProductoVentasDTO() {
    }

    // Constructor completo
    public ProductoVentasDTO(Long idProducto, String nombreProducto, Long cantidadVendida, Double totalVentas) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
        this.totalVentas = totalVentas;
    }

    // Getters y Setters
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Long getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(Long cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public Double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }
}
