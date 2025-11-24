// Asi mismo este sirve para definir el repositorio de detalles de compra
package com.licoreria.licoreria0.repositorio;
import com.licoreria.licoreria0.modelo.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DetalleCompraRepositorio extends JpaRepository<DetalleCompra, Long> {
}