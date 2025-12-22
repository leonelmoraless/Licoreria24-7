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

### 3.2. Métricas de Calidad del Producto
Para hacer la calidad "medible", utilizaremos los siguientes indicadores (KPIs):

1.  **Densidad de Defectos:** (Nº de Bugs Encontrados / KLOC - Miles de Líneas de Código). *Meta: < 0.5 por KLOC.*
2.  **Cobertura de Pruebas (Test Coverage):** Porcentaje de código ejecutado por los tests. *Meta: > 80%.*
3.  **Tasa de Éxito de Pruebas (Pass Rate):** `(Tests Exitosos / Total Tests) * 100`. *Actual: 100% (59/59).*
4.  **Estabilidad del Build:** Porcentaje de construcciones (`mvn package`) exitosas en el primer intento.

---

## 4. Herramientas de Implementación

Para materializar esta propuesta, el proyecto utiliza la siguiente infraestructura tecnológica ya configurada:

*   **JUnit 5:** Framework para la ejecución de pruebas unitarias y de integración.
*   **Mockito:** Aislamiento de componentes para pruebas focalizadas.
*   **Maven Surefire:** Generación de reportes de ejecución y detección de regresiones.
*   **H2 Database:** Validación de integridad de datos en entorno controlado.

---
**Elaborado por:** Equipo de Desarrollo - Licorería 24/7
