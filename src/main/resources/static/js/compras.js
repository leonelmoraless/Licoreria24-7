
/* manejar la interfaz de registro de compras, gestión del carrito de compras,
 * y envío de datos al backend 
 */


document.addEventListener("DOMContentLoaded", function () {
    const selectProveedor = document.getElementById("selectProveedor");
    const selectProducto = document.getElementById("selectProducto");
    const inputCantidad = document.getElementById("compraCantidad");
    const inputPrecio = document.getElementById("compraPrecio");
    const btnAgregar = document.getElementById("btnAgregarProducto");
    const btnRegistrar = document.getElementById("btnRegistrarCompra");
    const carritoBody = document.getElementById("carrito-compras-body");
    const carritoTotal = document.getElementById("carrito-compras-total");
    const mensajeCompra = document.getElementById("mensajeCompra");

    // estado de la aplicación
    let carrito = []; 

    //Cambio de proveedor -> Filtrar Productos
    selectProveedor.addEventListener("change", function () {
        const idProveedor = parseInt(this.value);
        filtrarProductosPorProveedor(idProveedor);
    });

    // Cambio de producto - rellenar Precio y Autoseleccionar Proveedor
    selectProducto.addEventListener("change", function () {
        const idProducto = parseInt(this.value);

        if (!idProducto) {
            inputPrecio.value = "";
            return;
        }

        const producto = allProductos.find(p => p.idProducto === idProducto);
        // rellana el precio de la compra y auto-selecciona el proveedor
        if (producto) {
            inputPrecio.value = producto.precioCompra.toFixed(2);
            if (selectProveedor.value != producto.proveedorId) {
                selectProveedor.value = producto.proveedorId;
            }
        }
    });

    // agregar producto al carrito
    btnAgregar.addEventListener("click", function () {
        const idProducto = parseInt(selectProducto.value);
        const cantidad = parseInt(inputCantidad.value);
        const precio = parseFloat(inputPrecio.value);

        if (!idProducto || !selectProveedor.value || cantidad <= 0 || isNaN(precio) || precio < 0) {
            alert("Por favor, seleccione un Proveedor y un Producto, y verifique la Cantidad.");
            return;
        }

        const producto = allProductos.find(p => p.idProducto === idProducto);

        // agregar producto en el carrito, si ya existe se suma 
        const itemExistente = carrito.find(item => item.id === idProducto);
        if (itemExistente) {
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

    // Botón registrar la compra
    btnRegistrar.addEventListener("click", function () {
        const idProveedor = parseInt(selectProveedor.value);

        if (!idProveedor || carrito.length === 0) {
            alert("Debe seleccionar un proveedor y agregar al menos un producto.");
            return;
        }

        const itemsParaBackend = carrito.map(item => ({
            idProducto: item.id,
            cantidad: item.cantidad,
            precioUnitario: item.precio
        }));

        const datosCompra = {
            idProveedor: idProveedor,
            items: itemsParaBackend
        };

        mensajeCompra.innerHTML = `<div class="alert alert-info">Procesando compra...</div>`;
        btnRegistrar.disabled = true;

        // Envia los datos al backend por la api
        fetch('/api/compras/registrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datosCompra)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                return response.json();
            })
            .then(compraGuardada => {
                mensajeCompra.innerHTML = `<div class="alert alert-success">¡Compra #${compraGuardada.idCompra} registrada! Redirigiendo...</div>`;
                carrito = [];
                actualizarCarritoUI();

                setTimeout(() => {
                    window.location.href = '/historial-compras';
                }, 1500);
            })
            .catch(error => {
                mensajeCompra.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
            })
            .finally(() => {
                btnRegistrar.disabled = false;
            });
    });

    // boton de eliminar compra del carrito
    carritoBody.addEventListener("click", function (e) {
        const botonQuitar = e.target.closest('.btn-quitar-item');
        if (botonQuitar) {
            const index = botonQuitar.getAttribute('data-index');
            carrito.splice(index, 1); 
            actualizarCarritoUI();    
        }
    });

    //filtra los productos según el proveedor seleccionado
    function filtrarProductosPorProveedor(idProveedor) {
        
        const seleccionPrevia = selectProducto.value;


        selectProducto.innerHTML = '<option value="">Seleccione un producto...</option>';
        inputPrecio.value = "";

        let productosFiltrados = [];

        if (idProveedor) {
            productosFiltrados = allProductos.filter(p => p.proveedorId === idProveedor);
        } else {
            productosFiltrados = allProductos;
        }

        
        productosFiltrados.forEach(p => {
            const option = document.createElement("option");
            option.value = p.idProducto;
            option.textContent = p.nombre;
            selectProducto.appendChild(option);
        });

        // Restaurar selección previa si es posible
        if (seleccionPrevia && productosFiltrados.some(p => p.idProducto == seleccionPrevia)) {
            selectProducto.value = seleccionPrevia;
        }
    }

    //actualizar la interfaz del carrito
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

    filtrarProductosPorProveedor(0);


});
