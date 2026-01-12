/*
 * seguridad de la aplicacion, definiendo las rutas accesibles segun el rol del usuario,
 * asi como la configuracion del login y logout
 */
package com.licoreria.licoreria0.configuracion;

import com.licoreria.licoreria0.servicio.ServicioDetallesUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
        // Encargado de cargar los datos del usuario desde la base de datos
        @Autowired
        private ServicioDetallesUsuario servicioDetallesUsuario;

        // para encriptar contraseñas
        @Bean
        public static PasswordEncoder codificadorDeContrasena() {
                return new BCryptPasswordEncoder();
        }

        // Configura los filtros de seguridad, reglas y rutas del sistema

        @Bean
        public SecurityFilterChain cadenaDeFiltros(HttpSecurity http) throws Exception {
                http
                                // Deshabilitamos protección CSRF solo para la API
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

                                .authorizeHttpRequests(auth -> auth
                                                // Pagina de inicio del sistema
                                                .requestMatchers("/login", "/recuperar-contrasena", "/css/**", "/js/**",
                                                                "/imagenes/**", "/error")
                                                .permitAll()

                                                // Paginas del Empleado y del Administrador
                                                .requestMatchers("/productos/**", "/compras/**",
                                                                "/historial-compras/**", "/ventas/**",
                                                                "/historial-ventas/**", "/detalle_compra/**",
                                                                "/detalle_venta/**", "/clientes/**", "/analisis/**")
                                                .hasAnyAuthority("Empleado", "Administrador")

                                                // Paginas para el Administrador
                                                .requestMatchers("/usuario/**", "/proveedores/**")
                                                .hasAuthority("Administrador")

                                                // Pagina de inicio para usuarios autenticados para el home
                                                // y cambio de contraseña
                                                .requestMatchers("/", "/home", "/cambiar-contrasena").authenticated()

                                                // Requiere de autenticacion si una ruta no esta definida
                                                .anyRequest().authenticated())
                                // Configuración del formulario de inicio de sesion
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/home", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                // Configuración del cierre de sesion
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout=true")
                                                .permitAll());

                return http.build();
        }

        // sirve para configurar la autenticacion del usuario
        @Autowired
        public void configurarGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(servicioDetallesUsuario)
                                .passwordEncoder(codificadorDeContrasena());
        }
}
