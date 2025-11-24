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
      }
    });
  });
});
