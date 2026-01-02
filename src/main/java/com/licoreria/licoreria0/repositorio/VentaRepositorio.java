package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente_IdCliente(Long idCliente);
}
