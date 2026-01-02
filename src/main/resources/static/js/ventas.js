document.addEventListener("DOMContentLoaded", function () {
    const selectCliente = document.getElementById("selectCliente");
    const selectMetodoPago = document.getElementById("selectMetodoPago");
    const selectProducto = document.getElementById("selectProducto");
    const inputCantidad = document.getElementById("ventaCantidad");
    const inputPrecio = document.getElementById("ventaPrecio");
    const btnAgregar = document.getElementById("btnAgregarProducto");
    const btnRegistrar = document.getElementById("btnRegistrarVenta");
    const carritoBody = document.getElementById("carrito-ventas-body");
    const carritoTotal = document.getElementById("carrito-ventas-total");
    const mensajeVenta = document.getElementById("mensajeVenta");

    let carrito = [];

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
            itemExistente.subtotal = itemExistente.cantidad * itemExistente.precio;
        } else {
            const subtotal = precio * cantidad;
            carrito.push({
                id: idProducto,
                nombre: producto.nombre,
                cantidad: cantidad,
                precio: precio,
                subtotal: subtotal
            });
        }

        actualizarCarritoUI();

        // Reset campos
        selectProducto.value = "";
        inputCantidad.value = "1";
        inputPrecio.value = "";
    });

    // Registrar Venta
    btnRegistrar.addEventListener("click", function () {
        const idCliente = parseInt(selectCliente.value);
        const metodoPago = selectMetodoPago.value;

        if (!idCliente || carrito.length === 0) {
            alert("Debe seleccionar un cliente y agregar al menos un producto.");
            return;
        }

        const itemsParaBackend = carrito.map(item => ({
            idProducto: item.id,
            cantidad: item.cantidad,
            precioUnitario: item.precio
        }));

        const datosVenta = {
            idCliente: idCliente,
            metodoPago: metodoPago,
            items: itemsParaBackend
        };

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
            .then(ventaGuardada => {
                mensajeVenta.innerHTML = `<div class="alert alert-success">¡Venta #${ventaGuardada.idVenta} registrada! Redirigiendo...</div>`;
                carrito = [];
                actualizarCarritoUI();

                setTimeout(() => {
                    window.location.href = '/historial-ventas';
                }, 1500);
            })
            .catch(error => {
                mensajeVenta.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
            })
            .finally(() => {
                btnRegistrar.disabled = false;
            });
    });

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
        let totalGeneral = 0;

        carrito.forEach((item, index) => {
            totalGeneral += item.subtotal;
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${item.nombre}</td>
                <td>${item.cantidad}</td>
                <td>$${item.precio.toFixed(2)}</td>
                <td>$${item.subtotal.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-danger btn-quitar-item" data-index="${index}">
                        <i class="bi bi-trash-fill"></i>
                    </button>
                </td>
            `;
            carritoBody.appendChild(fila);
        });

        carritoTotal.textContent = `$${totalGeneral.toFixed(2)}`;
    }
});
