package com.licoreria.licoreria0.patrones.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

// Servicio que decide cual estrategia usar
@Service
public class ContextoPago {

    private final Map<String, MetodoPagoStrategy> estrategias = new HashMap<>();

    // Spring inyecta automaticamente todas las clases que implementen la interfaz
    // MetodoPagoStrategy
    @Autowired
    public ContextoPago(List<MetodoPagoStrategy> listaEstrategias) {
        // Llenamos el mapa para buscar rapido por nombre
        for (MetodoPagoStrategy estrategia : listaEstrategias) {
            estrategias.put(estrategia.obtenerNombre().toUpperCase(), estrategia);
        }
    }

    public MetodoPagoStrategy obtenerEstrategia(String metodo) {
        if (metodo == null)
            return estrategias.get("EFECTIVO"); // Default

        String clave = metodo.toUpperCase();
        return estrategias.getOrDefault(clave, estrategias.get("EFECTIVO"));
    }
}
