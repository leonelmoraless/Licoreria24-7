package com.licoreria.licoreria0.patrones.strategy;

import com.licoreria.licoreria0.modelo.Venta;
import org.springframework.stereotype.Component;

// Implementacion concreta para pagar con Tarjeta
@Component
public class PagoTarjetaStrategy implements MetodoPagoStrategy {

    @Override
    public void procesarPago(Venta venta, double monto) {
        // Logica dummy
        // Si aqui ocurre un error, lanzar RuntimeException para activar el rollback en
        // Facade
        // if (monto > 100000) throw new RuntimeException("Monto excede limite de
        // tarjeta");

        System.out.println("Procesando pago con TARJETA para la venta " + venta.getIdVenta() + " por monto: " + monto);
    }

    @Override
    public String obtenerNombre() {
        return "TARJETA";
    }
}
