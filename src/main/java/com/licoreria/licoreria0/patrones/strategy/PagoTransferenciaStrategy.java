package com.licoreria.licoreria0.patrones.strategy;

import com.licoreria.licoreria0.modelo.Venta;
import org.springframework.stereotype.Component;

/**
 * Implementacion concreta para pagar con Transferencia Bancaria
 */
@Component
public class PagoTransferenciaStrategy implements MetodoPagoStrategy {

    @Override
    public void procesarPago(Venta venta, double monto) {
        Long idVenta = (venta != null) ? venta.getIdVenta() : -1L;
        System.out.println("Procesando pago por TRANSFERENCIA para la venta " + idVenta + " por monto: " + monto);
    }

    @Override
    public String obtenerNombre() {
        return "TRANSFERENCIA";
    }
}
