// Repositorio de detalles de compra
package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DetalleCompraRepositorio extends JpaRepository<DetalleCompra, Long> {
    boolean existsByProducto_IdProducto(Long idProducto);

    /**
     * Calcula el total gastado en compras dentro de un rango de fechas
     */
    @Query("SELECT COALESCE(SUM(dc.subtotal), 0) FROM DetalleCompra dc " +
            "WHERE dc.compra.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalCompras(@Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}