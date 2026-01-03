package com.licoreria.licoreria0.repositorio;

import com.licoreria.licoreria0.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCedula(String cedula);

    Optional<Cliente> findByCorreo(String correo);
}
