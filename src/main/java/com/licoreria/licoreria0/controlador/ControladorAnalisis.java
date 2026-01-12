package com.licoreria.licoreria0.controlador;

import com.licoreria.licoreria0.modelo.DetalleVenta;
import com.licoreria.licoreria0.modelo.dto.GananciasDTO;
import com.licoreria.licoreria0.modelo.dto.ProductoVentasDTO;
import com.licoreria.licoreria0.repositorio.DetalleCompraRepositorio;
import com.licoreria.licoreria0.repositorio.DetalleVentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para manejar las peticiones de análisis de ventas y ganancias
 */
@Controller
@RequestMapping("/analisis")
public class ControladorAnalisis {

    private static final Logger logger = LoggerFactory.getLogger(ControladorAnalisis.class);

    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;

    @Autowired
    private DetalleCompraRepositorio detalleCompraRepositorio;

    /**
     * Retorna la vista HTML del dashboard de análisis
     */
    @GetMapping
    public String mostrarAnalisis() {
        return "analisis";
    }

    /**
     * Obtiene los productos más vendidos en un rango de fechas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin    Fecha de fin del período
     * @param limite      Número máximo de productos a retornar (default: 10)
     * @return Lista de ProductoVentasDTO
     */
    @GetMapping("/productos-mas-vendidos")
    @ResponseBody
    public ResponseEntity<List<ProductoVentasDTO>> obtenerProductosMasVendidos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam(defaultValue = "10") int limite) {

        try {
            // Ajustar fechas para incluir todo el día
            Date fechaInicioAjustada = ajustarFechaInicio(fechaInicio);
            Date fechaFinAjustada = ajustarFechaFin(fechaFin);

            logger.info("Consultando productos más vendidos desde {} hasta {}", fechaInicioAjustada, fechaFinAjustada);

            List<ProductoVentasDTO> productos = detalleVentaRepositorio
                    .findProductosMasVendidos(fechaInicioAjustada, fechaFinAjustada);

            logger.info("Encontrados {} productos", productos.size());

            // Limitar el número de resultados
            if (productos.size() > limite) {
                productos = productos.subList(0, limite);
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            logger.error("Error al obtener productos más vendidos", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Calcula el análisis de ganancias para un rango de fechas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin    Fecha de fin del período
     * @return GananciasDTO con el análisis completo
     */
    @GetMapping("/ganancias")
    @ResponseBody
    public ResponseEntity<GananciasDTO> obtenerAnalisisGanancias(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {

        try {
            // Ajustar fechas para incluir todo el día
            Date fechaInicioAjustada = ajustarFechaInicio(fechaInicio);
            Date fechaFinAjustada = ajustarFechaFin(fechaFin);

            logger.info("Calculando ganancias desde {} hasta {}", fechaInicioAjustada, fechaFinAjustada);

            // Obtener todos los detalles de venta en el rango de fechas
            List<DetalleVenta> detalles = detalleVentaRepositorio
                    .findDetallesByFechaRange(fechaInicioAjustada, fechaFinAjustada);

            logger.info("Encontrados {} detalles de venta", detalles.size());

            // Calcular total de ventas
            double totalVentas = 0.0;
            for (DetalleVenta detalle : detalles) {
                totalVentas += detalle.getCantidad() * detalle.getPrecio();
            }

            // Obtener el costo total de compras realizadas en el período
            Double totalCostos = detalleCompraRepositorio.calcularTotalCompras(
                    fechaInicioAjustada, fechaFinAjustada);
            if (totalCostos == null) {
                totalCostos = 0.0;
            }

            logger.info("Total ventas: {}, Total costos (compras): {}", totalVentas, totalCostos);

            // Calcular ganancia y porcentaje
            double gananciaTotal = totalVentas - totalCostos;
            double porcentajeGanancia = 0.0;

            if (totalCostos > 0) {
                porcentajeGanancia = (gananciaTotal / totalCostos) * 100;
            }

            // Crear y retornar el DTO
            GananciasDTO ganancias = new GananciasDTO(
                    totalVentas,
                    totalCostos,
                    gananciaTotal,
                    porcentajeGanancia,
                    fechaInicio,
                    fechaFin);

            return ResponseEntity.ok(ganancias);
        } catch (Exception e) {
            logger.error("Error al calcular ganancias", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Ajusta la fecha de inicio al comienzo del día (00:00:00)
     */
    private Date ajustarFechaInicio(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Ajusta la fecha de fin al final del día (23:59:59)
     */
    private Date ajustarFechaFin(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
