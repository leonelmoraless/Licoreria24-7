# Pruebas Funcionales - Licorería 24/7 (Caja Negra)

Este documento describe los escenarios de prueba funcional desde la perspectiva del usuario final, sin conocimiento de la implementación interna.

## Objetivo

Validar que el sistema cumple con los requisitos funcionales y ofrece una experiencia de usuario fluida e intuitiva.

---

## Escenario 1: Registro Completo de Producto

**Objetivo**: Verificar que un usuario puede registrar un nuevo producto exitosamente.

### Precondiciones
- El usuario ha iniciado sesión en el sistema
- Existe al menos un proveedor registrado en el sistema

### Pasos
1. Navegar a la URL: `http://localhost:8080/productos`
2. Verificar que se muestra la página de productos con:
   - Lista de productos existentes
   - Formulario de registro de nuevo producto
3. En el formulario de registro, completar los siguientes campos:
   - **Nombre**: "Ron Bacardi 750ml"
   - **Precio de Compra**: "15.50"
   - **Precio de Venta**: "20.00"
   - **Stock**: "10"
   - **Proveedor**: Seleccionar un proveedor de la lista desplegable
4. Hacer clic en el botón "Registrar Producto"

### Resultado Esperado
- ✅ El sistema muestra un mensaje de éxito: "Producto registrado exitosamente"
- ✅ El producto "Ron Bacardi 750ml" aparece en la lista de productos
- ✅ Los datos del producto coinciden con los ingresados
- ✅ El formulario se limpia para permitir un nuevo registro

---

## Escenario 2: Validación de Regla de Negocio - Precio de Venta

**Objetivo**: Verificar que el sistema valida correctamente la regla de negocio sobre precios.

### Precondiciones
- El usuario ha iniciado sesión en el sistema
- Existe al menos un proveedor registrado

### Pasos
1. Navegar a `http://localhost:8080/productos`
2. En el formulario de registro, completar:
   - **Nombre**: "Vodka Absolut"
   - **Precio de Compra**: "30.00"
   - **Precio de Venta**: "20.00" ⚠️ (Menor al precio de compra)
   - **Stock**: "5"
   - **Proveedor**: Seleccionar un proveedor
3. Hacer clic en "Registrar Producto"

### Resultado Esperado
- ❌ El sistema muestra un mensaje de error claro: "El precio de venta no puede ser menor al precio de compra"
- ❌ El producto NO se registra en la base de datos
- ✅ El usuario puede corregir el error y volver a intentar

---

## Escenario 3: Actualización de Producto

**Objetivo**: Verificar que un usuario puede actualizar los datos de un producto existente.

### Precondiciones
- El usuario ha iniciado sesión
- Existe al menos un producto registrado

### Pasos
1. Navegar a `http://localhost:8080/productos`
2. Localizar un producto en la lista (ejemplo: "Ron Bacardi 750ml")
3. Hacer clic en el botón "Editar" del producto
4. Modificar los siguientes campos:
   - **Nombre**: "Ron Bacardi 1L"
   - **Precio de Venta**: "25.00"
   - **Stock**: "15"
5. Hacer clic en "Actualizar Producto"

### Resultado Esperado
- ✅ El sistema muestra mensaje: "Producto actualizado exitosamente"
- ✅ Los cambios se reflejan inmediatamente en la lista de productos
- ✅ Al recargar la página, los cambios persisten

---

## Escenario 4: Flujo Completo de Compra

**Objetivo**: Verificar el proceso completo de registro de una compra y actualización de stock.

### Precondiciones
- El usuario ha iniciado sesión
- Existen productos registrados con stock inicial conocido
- Existe al menos un proveedor

### Pasos
1. Navegar a `http://localhost:8080/compras`
2. Verificar que se muestra el formulario de compra
3. Seleccionar un **Proveedor** de la lista desplegable
4. Agregar productos a la compra:
   - **Producto 1**: Ron Bacardi, Cantidad: 5, Precio Unitario: 15.00
   - **Producto 2**: Vodka Absolut, Cantidad: 3, Precio Unitario: 25.00
5. Verificar que el sistema calcula automáticamente:
   - Subtotal Producto 1: 75.00 (5 × 15.00)
   - Subtotal Producto 2: 75.00 (3 × 25.00)
   - **Total**: 150.00
6. Hacer clic en "Registrar Compra"

### Resultado Esperado
- ✅ El sistema muestra mensaje: "Compra registrada exitosamente"
- ✅ El stock de cada producto se incrementa correctamente:
  - Ron Bacardi: stock anterior + 5
  - Vodka Absolut: stock anterior + 3
- ✅ La compra aparece en el historial de compras (`/historial-compras`)
- ✅ Los detalles de la compra muestran todos los productos y el total correcto

---

## Escenario 5: Validación de Eliminación de Producto con Stock

**Objetivo**: Verificar que el sistema impide eliminar productos que tienen stock.

### Precondiciones
- El usuario ha iniciado sesión
- Existe un producto con stock mayor a cero

### Pasos
1. Navegar a `http://localhost:8080/productos`
2. Localizar un producto con stock > 0 (ejemplo: "Ron Bacardi" con stock 15)
3. Hacer clic en el botón "Eliminar" del producto
4. Confirmar la eliminación si se solicita

### Resultado Esperado
- ❌ El sistema muestra mensaje de error: "No se puede eliminar un producto con stock (15)"
- ❌ El producto NO se elimina de la base de datos
- ✅ El producto sigue visible en la lista

---

## Escenario 6: Eliminación Exitosa de Producto sin Stock

**Objetivo**: Verificar que se puede eliminar un producto cuando su stock es cero.

### Precondiciones
- El usuario ha iniciado sesión
- Existe un producto con stock = 0

### Pasos
1. Navegar a `http://localhost:8080/productos`
2. Si no existe un producto con stock 0:
   - Editar un producto existente
   - Cambiar su stock a 0
   - Guardar cambios
3. Hacer clic en "Eliminar" del producto con stock 0
4. Confirmar la eliminación

### Resultado Esperado
- ✅ El sistema muestra mensaje: "Producto eliminado exitosamente"
- ✅ El producto desaparece de la lista
- ✅ Al recargar la página, el producto no aparece

---

## Escenario 7: Eliminación de Compra y Reversión de Stock

**Objetivo**: Verificar que al eliminar una compra, el stock se revierte correctamente.

### Precondiciones
- El usuario ha iniciado sesión
- Existe al menos una compra registrada
- Se conoce el stock actual de los productos involucrados

### Pasos
1. Anotar el stock actual de los productos involucrados en una compra
2. Navegar a `http://localhost:8080/historial-compras`
3. Localizar una compra específica
4. Hacer clic en "Eliminar Compra"
5. Confirmar la eliminación
6. Navegar a `http://localhost:8080/productos`
7. Verificar el stock de los productos que estaban en la compra eliminada

### Resultado Esperado
- ✅ La compra se elimina del historial
- ✅ El stock de cada producto se reduce en la cantidad que se había comprado
- ✅ Ejemplo: Si se compró 5 unidades de Ron y se elimina la compra, el stock se reduce en 5

---

## Escenario 8: Usabilidad - Mensajes de Error Claros

**Objetivo**: Verificar que los mensajes de error son claros y ayudan al usuario.

### Pasos
1. Intentar registrar un producto con campos vacíos
2. Intentar registrar un producto con precio negativo
3. Intentar registrar un producto sin seleccionar proveedor

### Resultado Esperado
- ✅ Cada error muestra un mensaje específico y claro
- ✅ Los mensajes indican exactamente qué campo tiene el problema
- ✅ El usuario puede corregir fácilmente el error

---

## Escenario 9: Navegación y Flujo Intuitivo

**Objetivo**: Verificar que la navegación entre módulos es intuitiva.

### Pasos
1. Desde la página principal, navegar a Productos
2. Desde Productos, navegar a Compras
3. Desde Compras, navegar a Historial de Compras
4. Verificar que existe un menú o navegación clara

### Resultado Esperado
- ✅ La navegación es clara y accesible
- ✅ El usuario siempre sabe en qué módulo se encuentra
- ✅ Es fácil volver a la página anterior

---

## Escenario 10: Persistencia de Datos

**Objetivo**: Verificar que los datos persisten correctamente.

### Pasos
1. Registrar un nuevo producto
2. Cerrar el navegador completamente
3. Volver a abrir el navegador
4. Iniciar sesión nuevamente
5. Navegar a la página de productos

### Resultado Esperado
- ✅ El producto registrado anteriormente sigue presente
- ✅ Todos los datos del producto se mantienen correctos
- ✅ No hay pérdida de información

---

## Criterios de Aceptación General

Para que el sistema pase las pruebas funcionales (Caja Negra), debe cumplir:

- ✅ **Funcionalidad**: Todas las operaciones CRUD funcionan correctamente
- ✅ **Validaciones**: Las reglas de negocio se aplican consistentemente
- ✅ **Usabilidad**: Los mensajes son claros y la interfaz es intuitiva
- ✅ **Persistencia**: Los datos se guardan y recuperan correctamente
- ✅ **Integridad**: Las relaciones entre entidades se mantienen (productos-proveedores, compras-productos)
- ✅ **Consistencia**: El stock se actualiza correctamente en todas las operaciones

---

## Notas para el Evaluador

- Estos escenarios deben ejecutarse manualmente con el sistema en ejecución
- Se recomienda usar datos de prueba diferentes en cada ejecución
- Documentar cualquier comportamiento inesperado o error encontrado
- Tomar capturas de pantalla de los resultados para evidencia
