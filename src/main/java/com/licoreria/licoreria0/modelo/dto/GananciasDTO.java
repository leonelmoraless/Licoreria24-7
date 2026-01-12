package com.licoreria.licoreria0.modelo.dto;

import java.util.Date;

/**
 * DTO para representar el análisis de ganancias en un período de tiempo
 */
public class GananciasDTO {

    private Double totalVentas;
    private Double totalCostos;
    private Double gananciaTotal;
    private Double porcentajeGanancia;
    private Date fechaInicio;
    private Date fechaFin;

    // Constructor vacío
    public GananciasDTO() {
    }

    // Constructor completo
    public GananciasDTO(Double totalVentas, Double totalCostos, Double gananciaTotal,
            Double porcentajeGanancia, Date fechaInicio, Date fechaFin) {
        this.totalVentas = totalVentas;
        this.totalCostos = totalCostos;
        this.gananciaTotal = gananciaTotal;
        this.porcentajeGanancia = porcentajeGanancia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public Double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Double getTotalCostos() {
        return totalCostos;
    }

    public void setTotalCostos(Double totalCostos) {
        this.totalCostos = totalCostos;
    }

    public Double getGananciaTotal() {
        return gananciaTotal;
    }

    public void setGananciaTotal(Double gananciaTotal) {
        this.gananciaTotal = gananciaTotal;
    }

    public Double getPorcentajeGanancia() {
        return porcentajeGanancia;
    }

    public void setPorcentajeGanancia(Double porcentajeGanancia) {
        this.porcentajeGanancia = porcentajeGanancia;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
