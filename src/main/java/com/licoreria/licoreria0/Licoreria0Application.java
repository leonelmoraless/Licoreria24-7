/*
 * Es la que arranca la aplicacion Spring Boot para entrar el local host 8080 definido en
el application.properties
 */
package com.licoreria.licoreria0;

import com.licoreria.licoreria0.modelo.Usuario;
import com.licoreria.licoreria0.repositorio.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableAsync
@SpringBootApplication
public class Licoreria0Application {

    public static void main(String[] args) {
        SpringApplication.run(Licoreria0Application.class, args);
    }

    @Bean
    @org.springframework.context.annotation.Profile("!test")
    public CommandLineRunner initData(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@admin.com";
            if (usuarioRepositorio.findByCorreo(email).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setCorreo(email);
                admin.setContrasena(passwordEncoder.encode("admin"));
                admin.setRol("Administrador");
                usuarioRepositorio.save(admin);
                System.out.println("Usuario administrador creado: " + email + " / admin");
            }
        };
    }

}