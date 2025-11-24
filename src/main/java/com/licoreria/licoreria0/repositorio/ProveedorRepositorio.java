
// Este archivo sirve para definir el repositorio de proveedores
package com.licoreria.licoreria0.repositorio;
import com.licoreria.licoreria0.modelo.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRuc(String ruc);
}