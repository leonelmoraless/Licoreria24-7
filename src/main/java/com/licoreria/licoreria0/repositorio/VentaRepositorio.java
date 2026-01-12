package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente_IdCliente(Long idCliente);

    boolean existsByCliente_IdCliente(Long idCliente);

    /**
     * Obtiene todas las ventas en un rango de fechas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin    Fecha de fin del período
     * @return Lista de ventas en el rango especificado
     */
    List<Venta> findByFechaBetween(Date fechaInicio, Date fechaFin);
}
