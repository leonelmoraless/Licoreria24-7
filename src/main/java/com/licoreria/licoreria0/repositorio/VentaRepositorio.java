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

    /**
     * Obtiene una venta con sus pagos cargados (evita lazy loading)
     * 
     * @param idVenta ID de la venta
     * @return Venta con pagos cargados
     */
    @org.springframework.data.jpa.repository.Query("SELECT v FROM Venta v LEFT JOIN FETCH v.pagos WHERE v.idVenta = :idVenta")
    java.util.Optional<Venta> findByIdWithPagos(
            @org.springframework.data.repository.query.Param("idVenta") Long idVenta);
}
