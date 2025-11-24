
// Este archivo sirve para definir el repositorio de proveedores
package com.licoreria.licoreria0.repositorio;
import com.licoreria.licoreria0.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}

//Estos archivos de repositorio mas que todo estan por documentacion para entender mejor el codigo
//No se han realizado cambios funcionales en ellos 
