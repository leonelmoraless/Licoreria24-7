/*
Manejar los modales de eliminar y editar
*/

document.addEventListener("DOMContentLoaded", function () {
  // Busca para eliminar algo
  const modalesEliminar = document.querySelectorAll('.modal[id*="Eliminar"]');

  modalesEliminar.forEach((modal) => {
    modal.addEventListener("show.bs.modal", function (evento) {
      const boton = evento.relatedTarget;

      if (boton && boton.classList.contains("btn-eliminar")) {
        // Lee los datos guardados en el botón
        const id = boton.getAttribute("data-id");
        const nombre = boton.getAttribute("data-nombre");

        // Busca los campos del modal
        const inputId = modal.querySelector('input[id*="Eliminar"]');
        const spanNombre = modal.querySelector('strong[id*="Eliminar"]');

        // se asigna los valores
        if (inputId) inputId.value = id;
        if (spanNombre) spanNombre.textContent = nombre;
      }
    });
  });

  // Busca para editar algo
  const modalesEditar = document.querySelectorAll('.modal[id*="Editar"]');

  modalesEditar.forEach((modal) => {
    modal.addEventListener("show.bs.modal", function (evento) {
      const boton = evento.relatedTarget;

      if (boton && boton.classList.contains("btn-editar")) {
        // Lee los datos guardados en el botón
        const datos = boton.dataset;

        // Busca los campos del modal
        const inputsYSelects = modal.querySelectorAll("input, select");

        // Rellena los campos con los datos
        inputsYSelects.forEach((input) => {
          const nombreCampo = input.id.replace("edit-", "");

          if (datos[nombreCampo] !== undefined) {
            input.value = datos[nombreCampo];
          }
        });

        // Calcular porcentaje de ganancia inicial para editar
        const inputPrecioCompra = document.getElementById('edit-precioCompra');
        const inputPrecioVenta = document.getElementById('edit-precioVenta');
        const porcentajeInput = document.getElementById('edit-porcentajeGanancia');

        if (inputPrecioCompra && inputPrecioVenta && porcentajeInput) {
          const precioCompra = parseFloat(inputPrecioCompra.value) || 0;
          const precioVenta = parseFloat(inputPrecioVenta.value) || 0;

          if (precioCompra > 0 && precioVenta > 0) {
            const ganancia = ((precioVenta - precioCompra) / precioCompra) * 100;
            porcentajeInput.value = ganancia.toFixed(2);
          } else {
            porcentajeInput.value = '';
          }
        }
      }
    });
  });

  // Lógica para calcular precio de venta basado en porcentaje de ganancia
  function calcularPrecioVenta(idCompra, idPorcentaje, idVenta) {
    const inputCompra = document.getElementById(idCompra);
    const inputPorcentaje = document.getElementById(idPorcentaje);
    const inputVenta = document.getElementById(idVenta);

    function actualizarPrecio() {
      const compra = parseFloat(inputCompra.value) || 0;
      const porcentaje = parseFloat(inputPorcentaje.value) || 0;

      if (compra >= 0 && porcentaje >= 0) {
        const venta = compra + (compra * (porcentaje / 100));
        inputVenta.value = venta.toFixed(2);
      }
    }

    if (inputCompra && inputPorcentaje && inputVenta) {
      inputCompra.addEventListener('input', actualizarPrecio);
      inputPorcentaje.addEventListener('input', actualizarPrecio);
    }
  }

  // Inicializar cálculos para registrar y editar
  calcularPrecioVenta('precioCompra', 'porcentajeGanancia', 'precioVenta');
  calcularPrecioVenta('edit-precioCompra', 'edit-porcentajeGanancia', 'edit-precioVenta');
});
