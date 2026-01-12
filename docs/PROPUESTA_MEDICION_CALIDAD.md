# 📉 Propuesta de Medición y Aseguramiento de Calidad - Licorería 24/7

> **Estándares:** IEEE 730 (SQA) | CMMI-DEV V1.3 (PPQA)

## 1. Visión General

Esta propuesta define la estrategia para garantizar que el software **Licorería 24/7** cumpla con los más altos estándares de fiabilidad y funcionalidad. Se integran procesos de aseguramiento (Process Assurance) y validación del producto final (Product Assurance).

---

## 2. Aseguramiento de Calidad del Software (SQA - IEEE 730)

Siguiendo el estándar **IEEE 730**, el proceso de calidad no es una fase final, sino una actividad continua desde el inicio del desarrollo.

### 2.1. Actividades de Control
Para garantizar que "las actividades estén alineadas con el plan", implementaremos:

*   **Auditorías de Código Estático:** Revisión automática de estilo y buenas prácticas en cada compilación (Checkstyle/SonarQube).
*   **Revisiones por Pares (Peer Reviews):** Validación manual de la lógica antes de fusionar cambios en el repositorio (Pull Requests).
*   **Trazabilidad de Requisitos:** Cada funcionalidad (Ej: "Registrar Venta") debe tener asociado al menos un caso de prueba unitario y uno de integración.

### 2.2. Hitos de Calidad
*   **Hito 1: Verificación Unitaria (Completado):** 100% de éxito en pruebas de lógica interna (Factory, Modelos).
*   **Hito 2: Verificación de Integración (Completado):** 100% de éxito en pruebas de flujo de datos (Controladores <-> BD).
*   **Hito 3: Validación de Usuario:** Aprobación formal mediante las pruebas funcionales documentadas.

---

## 3. Aseguramiento del Producto (CMMI-DEV V1.3 - PPQA)

Según **CMMI-DEV**, definimos criterios objetivos para evaluar cada funcionalidad crítica del sistema.

### 3.1. Evaluación por Componente Funcional

| Funcionalidad | Método de Evaluación | Criterio de Aceptación |
| :--- | :--- | :--- |
| **Gestión de Productos** | Tests Automáticos (`FacadeProductoTest`) | Creación, edición y eliminación validan stock y precios lógicos. |
| **Control de Inventario** | Tests de Integración (`ProductoRepositorio`) | El stock nunca es negativo; las transacciones son atómicas. |
| **Registro de Ventas** | Tests End-to-End (`ControladorCompra`) | Cálculo exacto de subtotales y totales; reducción inmediata de stock. |
| **Seguridad** | Tests de Seguridad (`TestSecurityConfig`) | Accesos no autorizados son bloqueados (403/401) en producción. |

### 3.2. Métricas de Calidad del Producto (Indicadores Clave)
Para medir qué tan bueno es nuestro software con números reales, usamos estos indicadores:

1.  **Densidad de Errores (¿Qué tan limpio es el código?):**
    *   *Qué mide:* Cuántos errores (bugs) encontramos por cada 1,000 líneas de código.
    *   *Objetivo:* Menos de 0.5 errores. (Significa que es muy estable).

2.  **Cobertura de Pruebas (¿Cuánto código probamos?):**
    *   *Qué mide:* El porcentaje de nuestro código que es ejecutado y verificado por los tests automáticos.
    *   *Objetivo:* Más del 80%. (Significa que casi todo el código ha sido verificado).

3.  **Tasa de Éxito de Pruebas (¿Funciona lo que probamos?):**
    *   *Qué mide:* De todas las pruebas que ejecutamos, ¿cuántas pasaron exitosamente?
    *   *Resultado Actual:* **100%** (137 pruebas exitosas de 137). ¡Excelente resultado!

4.  **Estabilidad de Compilación (¿El proyecto funciona al armarlo?):**
    *   *Qué mide:* Si el proyecto se puede compilar y empaquetar (`mvn package`) sin errores a la primera.
    *   *Objetivo:* 100% de éxito en la compilación.

---

## 4. Herramientas de Implementación

Para materializar esta propuesta, el proyecto utiliza la siguiente infraestructura tecnológica ya configurada:

*   **JUnit 5:** Framework para la ejecución de pruebas unitarias y de integración.
*   **Mockito:** Aislamiento de componentes para pruebas focalizadas.
*   **Maven Surefire:** Generación de reportes de ejecución y detección de regresiones.
*   **H2 Database:** Validación de integridad de datos en entorno controlado.

---
**Elaborado por:** Equipo de Desarrollo - Licorería 24/7
