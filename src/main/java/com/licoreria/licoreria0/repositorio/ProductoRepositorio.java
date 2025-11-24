
// Este archivo sirve para definir el repositorio de productos
package com.licoreria.licoreria0.repositorio;
import com.licoreria.licoreria0.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
}