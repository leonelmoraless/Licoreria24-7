package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, Long> {
}
