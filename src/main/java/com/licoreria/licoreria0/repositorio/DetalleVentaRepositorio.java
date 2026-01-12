package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.DetalleVenta;
import com.licoreria.licoreria0.modelo.dto.ProductoVentasDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DetalleVentaRepositorio extends JpaRepository<DetalleVenta, Long> {

    /**
     * Obtiene los productos más vendidos en un rango de fechas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin    Fecha de fin del período
     * @return Lista de ProductoVentasDTO ordenada por cantidad vendida
     *         (descendente)
     */
    @Query("SELECT new com.licoreria.licoreria0.modelo.dto.ProductoVentasDTO(" +
            "p.idProducto, p.nombre, SUM(dv.cantidad), SUM(dv.cantidad * dv.precio)) " +
            "FROM DetalleVenta dv " +
            "JOIN dv.producto p " +
            "JOIN dv.venta v " +
            "WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY p.idProducto, p.nombre " +
            "ORDER BY SUM(dv.cantidad) DESC")
    List<ProductoVentasDTO> findProductosMasVendidos(
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);

    /**
     * Obtiene todos los detalles de venta en un rango de fechas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin    Fecha de fin del período
     * @return Lista de DetalleVenta
     */
    @Query("SELECT dv FROM DetalleVenta dv " +
            "JOIN dv.venta v " +
            "WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<DetalleVenta> findDetallesByFechaRange(
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}
