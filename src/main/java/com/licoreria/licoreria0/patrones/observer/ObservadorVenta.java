package com.licoreria.licoreria0.patrones.observer;

import com.licoreria.licoreria0.modelo.Venta;

// Interfaz para escuchar cuando ocurre una venta
public interface ObservadorVenta {
    void notificarVenta(Venta venta) throws Exception;
}
