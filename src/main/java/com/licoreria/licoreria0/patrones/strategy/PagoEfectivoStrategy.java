package com.licoreria.licoreria0.patrones.strategy;

import com.licoreria.licoreria0.modelo.Venta;
import org.springframework.stereotype.Component;

// Implementacion concreta para pagar con Efectivo
@Component
public class PagoEfectivoStrategy implements MetodoPagoStrategy {

    @Override
    public void procesarPago(Venta venta, double monto) {
        // Aqui podria ir logica adicional, como validar billetes, calcular cambio, etc.
        System.out.println("Procesando pago en EFECTIVO para la venta " + venta.getIdVenta() + " por monto: " + monto);
    }

    @Override
    public String obtenerNombre() {
        return "EFECTIVO";
    }
}
