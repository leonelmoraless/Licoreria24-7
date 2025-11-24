
// Este archivo sirve para definir el repositorio de compras
package com.licoreria.licoreria0.repositorio;
import com.licoreria.licoreria0.modelo.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CompraRepositorio extends JpaRepository<Compra, Long> {
}