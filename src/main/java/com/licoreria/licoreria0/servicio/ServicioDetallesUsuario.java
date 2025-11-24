/*
 * este conecta con el facade para cargar los detalles del usuario para spring security 
 */
package com.licoreria.licoreria0.servicio;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.patrones.facade.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServicioDetallesUsuario implements UserDetailsService {

    @Autowired
    private Facade facade; 

    // llama al facade para buscar el usario por su correo, comprueba el rol 
    // y retorna el user con sus datos para spring security
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
       
        Usuario usuario = facade.buscarUsuarioParaLogin(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(usuario.getRol()));

        return new User(
                usuario.getCorreo(), 
                usuario.getContrasena(),
                authorities
        );
    }
}