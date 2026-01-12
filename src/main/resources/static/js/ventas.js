document.addEventListener("DOMContentLoaded", function () {
    const selectCliente = document.getElementById("selectCliente");
    const selectMetodoPago = document.getElementById("selectMetodoPago");
    const selectProducto = document.getElementById("selectProducto");
    const inputCantidad = document.getElementById("ventaCantidad");
    const inputPrecio = document.getElementById("ventaPrecio");
    const inputDescuento = document.getElementById("ventaDescuento");
    const inputIva = document.getElementById("inputIva");
    const btnAgregar = document.getElementById("btnAgregarProducto");
    const btnRegistrar = document.getElementById("btnRegistrarVenta");
    const carritoBody = document.getElementById("carrito-ventas-body");
    const carritoSubtotal = document.getElementById("carrito-subtotal");
    const carritoIva = document.getElementById("carrito-iva");
    const carritoTotal = document.getElementById("carrito-ventas-total");
    const ivaPercentSpan = document.getElementById("ivaPercent");
    const mensajeVenta = document.getElementById("mensajeVenta");

    // Elementos de transferencia
    const camposTransferencia = document.getElementById("camposTransferencia");
    const inputNumeroTransferencia = document.getElementById("inputNumeroTransferencia");
    const inputComprobante = document.getElementById("inputComprobante");
    const imgPreview = document.getElementById("imgPreview");

    let carrito = [];
    let comprobanteBase64 = null;

    // Llenar select de productos al cargar
    function llenarProductos() {
        selectProducto.innerHTML = '<option value="">Seleccione un producto...</option>';
        allProductos.forEach(p => {
            const option = document.createElement("option");
            option.value = p.idProducto;
            option.textContent = p.nombre + " (Stock: " + p.stock + ")";
            selectProducto.appendChild(option);
        });
    }

    llenarProductos();

    // Mostrar/ocultar campos de transferencia
    selectMetodoPago.addEventListener("change", function () {
        if (this.value === "Transferencia") {
            camposTransferencia.style.display = "flex";
        } else {
            camposTransferencia.style.display = "none";
            inputNumeroTransferencia.value = "";
            inputComprobante.value = "";
            imgPreview.style.display = "none";
            comprobanteBase64 = null;
        }
    });

    // Preview y conversión a Base64 del comprobante
    inputComprobante.addEventListener("change", function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (event) {
                comprobanteBase64 = event.target.result;
                imgPreview.src = comprobanteBase64;
                imgPreview.style.display = "block";
            };
            reader.readAsDataURL(file);
        }
    });

    // Actualizar IVA en el resumen cuando cambie
    inputIva.addEventListener("change", function () {
        ivaPercentSpan.textContent = this.value || "15";
        actualizarCarritoUI();
    });

    // Cambio de producto - rellenar Precio Venta
    selectProducto.addEventListener("change", function () {
        const idProducto = parseInt(this.value);

        if (!idProducto) {
            inputPrecio.value = "";
            return;
        }

        const producto = allProductos.find(p => p.idProducto === idProducto);
        if (producto) {
            inputPrecio.value = producto.precioVenta.toFixed(2);
        }
    });

    // Agregar producto al carrito
    btnAgregar.addEventListener("click", function () {
        const idProducto = parseInt(selectProducto.value);
        const cantidad = parseInt(inputCantidad.value);
        const precio = parseFloat(inputPrecio.value);
        const descuento = parseFloat(inputDescuento.value) || 0;

        if (!idProducto || cantidad <= 0 || isNaN(precio)) {
            alert("Por favor, seleccione un Producto y verifique la Cantidad.");
            return;
        }

        const producto = allProductos.find(p => p.idProducto === idProducto);

        // Validar Stock
        if (producto.stock < cantidad) {
            alert("Stock insuficiente. Disponible: " + producto.stock);
            return;
        }

        // Validar si ya se agregó y la suma supera el stock
        const itemExistente = carrito.find(item => item.id === idProducto);
        if (itemExistente) {
            if (producto.stock < (itemExistente.cantidad + cantidad)) {
                alert("Stock insuficiente para agregar más cantidad. Disponible: " + producto.stock);
                return;
            }
            itemExistente.cantidad += cantidad;
            itemExistente.descuento = descuento; // Actualizar descuento
            const subtotalSinDesc = itemExistente.cantidad * itemExistente.precio;
            const descuentoMonto = subtotalSinDesc * (descuento / 100);
            itemExistente.subtotal = subtotalSinDesc - descuentoMonto;
        } else {
            const subtotalSinDesc = precio * cantidad;
            const descuentoMonto = subtotalSinDesc * (descuento / 100);
            const subtotal = subtotalSinDesc - descuentoMonto;

            carrito.push({
                id: idProducto,
                nombre: producto.nombre,
                cantidad: cantidad,
                precio: precio,
                descuento: descuento,
                subtotal: subtotal
            });
        }

        actualizarCarritoUI();

        // Reset campos
        selectProducto.value = "";
        inputCantidad.value = "1";
        inputPrecio.value = "";
        inputDescuento.value = "0";
    });

    // Registrar Venta
    btnRegistrar.addEventListener("click", function () {
        const idCliente = parseInt(selectCliente.value);
        const metodoPago = selectMetodoPago.value;
        const iva = parseFloat(inputIva.value) || 15;

        if (!idCliente || carrito.length === 0) {
            alert("Debe seleccionar un cliente y agregar al menos un producto.");
            return;
        }

        // Validar campos de transferencia
        if (metodoPago === "Transferencia") {
            const numTransferencia = inputNumeroTransferencia.value.trim();
            if (!numTransferencia) {
                alert("Por favor ingrese el número de transferencia.");
                return;
            }
            if (!comprobanteBase64) {
                alert("Por favor suba la captura del comprobante de transferencia.");
                return;
            }
        }

        const itemsParaBackend = carrito.map(item => ({
            idProducto: item.id,
            cantidad: item.cantidad,
            precioUnitario: item.precio,
            descuento: item.descuento
        }));

        const datosVenta = {
            idCliente: idCliente,
            metodoPago: metodoPago,
            iva: iva,
            items: itemsParaBackend
        };

        // Agregar datos de transferencia si aplica
        if (metodoPago === "Transferencia") {
            datosVenta.numeroTransferencia = inputNumeroTransferencia.value.trim();
            datosVenta.comprobanteBase64 = comprobanteBase64;
        }

        mensajeVenta.innerHTML = `<div class="alert alert-info">Procesando venta...</div>`;
        btnRegistrar.disabled = true;

        fetch('/api/ventas/registrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datosVenta)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                return response.json();
            })
            .then(ventaDTO => {
                // USAR TICKET UNIVERSAL
                // Pasamos callback para recargar página al cerrar
                window.mostrarTicketUniversal(ventaDTO, function () {
                    window.location.reload();
                });

                // Limpiar el carrito en segundo plano (aunque reload lo hará)
                carrito = [];
                comprobanteBase64 = null;
                actualizarCarritoUI();
                mensajeVenta.innerHTML = "";
            })
            .catch(error => {
                mensajeVenta.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
            })
            .finally(() => {
                btnRegistrar.disabled = false;
            });
    });

 main
    // Función auxiliar para rellenar el HTML del modal
    function llenarTicket(dto) {
        document.getElementById('ticketId').innerText = dto.idVenta;
        document.getElementById('ticketCliente').innerText = dto.nombreCliente;
        document.getElementById('ticketFecha').innerText = dto.fecha;

        document.getElementById('ticketSubtotal').innerText = dto.subtotal.toFixed(2);
        document.getElementById('ticketDescuento').innerText = dto.totalDescuento.toFixed(2);
        document.getElementById('ticketIva').innerText = dto.montoIva.toFixed(2);
        document.getElementById('ticketTotal').innerText = dto.total.toFixed(2);

        // Info Transferencia
        const secTransferencia = document.getElementById('infoTransferenciaTicket');
        if (dto.numeroTransferencia) {
            secTransferencia.style.display = 'block';
            document.getElementById('ticketRefTransferencia').innerText = dto.numeroTransferencia;

            if (dto.rutaComprobante) {
                // Ajustar ruta si es relativa
                let ruta = dto.rutaComprobante;
                if (!ruta.startsWith('http') && !ruta.startsWith('/')) {
                    // Si es una ruta de archivo local del servidor, necesitamos un endpoint para servirla
                    // O si la guardamos en uploads/..., necesitamos que static sirva esa carpeta.
                    // Por simplicidad, asumiremos que Spring sirve "uploads" si configuramos, 
                    // pero como no tenemos configuración de recursos estáticos extra,
                    // una opción rápida es NO mostrar la imagen si no es URL pública, 
                    // PERO el usuario quiere verla.
                    // TRUCO: Si la ruta es absoluta del sistema, no se puede ver en navegador.
                    // SOLUCIÓN: En este JS no podemos resolver eso fácilmente sin backend.
                    // PONIENDO UN PLACEHOLDER O MENSAJE SI NO ES ACCESIBLE.
                    document.getElementById('ticketImgComprobante').style.display = 'none'; // ocultar por ahora si es path local
                    // TODO: Implementar Controller para servir imágenes de uploads
                }
            } else {
                document.getElementById('ticketImgComprobante').style.display = 'none';
            }
        } else {
            secTransferencia.style.display = 'none';
        }

        const tbody = document.getElementById('ticketItems');
        tbody.innerHTML = '';

        dto.items.forEach(item => {
            let descTxt = item.descuento > 0 ? ` <small class="text-muted">(-${item.descuento}%)</small>` : '';
            let fila = `
                <tr>
                    <td>${item.producto}</td>
                    <td class="text-end">${item.cantidad}</td>
                    <td class="text-end">$${item.subtotal.toFixed(2)}${descTxt}</td>
                </tr>
            `;
            tbody.innerHTML += fila;
        });
    }

    // Funciones globales para los botones del modal
    window.imprimirTicket = function () {
        window.print();
    };

    window.cerrarVenta = function () {
        window.location.reload();
    };

    // (Funcion llenarTicket removida - ya no se usa)

    // Quitar item del carrito
 main

    // Quitar item del carrito
    carritoBody.addEventListener("click", function (e) {
        const botonQuitar = e.target.closest('.btn-quitar-item');
        if (botonQuitar) {
            const index = botonQuitar.getAttribute('data-index');
            carrito.splice(index, 1);
            actualizarCarritoUI();
        }
    });

    function actualizarCarritoUI() {
        carritoBody.innerHTML = "";
        let subtotalGeneral = 0;

        carrito.forEach((item, index) => {
            subtotalGeneral += item.subtotal;
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${item.nombre}</td>
                <td>${item.cantidad}</td>
                <td>$${item.precio.toFixed(2)}</td>
                <td>${item.descuento.toFixed(1)}%</td>
                <td>$${item.subtotal.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-danger btn-quitar-item" data-index="${index}">
                        <i class="bi bi-trash-fill"></i>
                    </button>
                </td>
            `;
            carritoBody.appendChild(fila);
        });

        // Calcular IVA y total
        const ivaPorcentaje = parseFloat(inputIva.value) || 15;
        const montoIva = subtotalGeneral * (ivaPorcentaje / 100);
        const totalFinal = subtotalGeneral + montoIva;

        ivaPercentSpan.textContent = ivaPorcentaje;
        carritoSubtotal.textContent = `$${subtotalGeneral.toFixed(2)}`;
        carritoIva.textContent = `$${montoIva.toFixed(2)}`;
        carritoTotal.textContent = `$${totalFinal.toFixed(2)}`;
    }
});
