package com.licoreria.licoreria0.patrones.strategy;

import com.licoreria.licoreria0.modelo.Venta;

// Interfaz comun para todos los metodos de pago
public interface MetodoPagoStrategy {
    void procesarPago(Venta venta, double monto); // Las implementaciones deben lanzar RuntimeException si fallan

    String obtenerNombre();
}
