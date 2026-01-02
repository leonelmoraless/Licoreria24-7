package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepositorio extends JpaRepository<DetalleVenta, Long> {
}
