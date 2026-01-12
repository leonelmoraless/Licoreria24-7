// analisis.js - Lógica para el módulo de análisis de ventas y ganancias

// Variables globales para los gráficos
let graficoGanancias = null;
let graficoProductos = null;

// Inicialización cuando el DOM está listo
document.addEventListener('DOMContentLoaded', function () {
    inicializarFechas();
    configurarEventos();
    cargarAnalisisInicial();
});

/**
 * Inicializa las fechas por defecto (último mes)
 */
function inicializarFechas() {
    const hoy = new Date();
    const haceUnMes = new Date();
    haceUnMes.setMonth(haceUnMes.getMonth() - 1);

    document.getElementById('fechaFin').valueAsDate = hoy;
    document.getElementById('fechaInicio').valueAsDate = haceUnMes;
}

/**
 * Configura los event listeners
 */
function configurarEventos() {
    // Cambio de tipo de período
    document.getElementById('tipoPeriodo').addEventListener('change', function () {
        actualizarCamposFecha(this.value);
    });

    // Botón aplicar filtro
    document.getElementById('btnAplicarFiltro').addEventListener('click', function () {
        cargarAnalisis();
    });
}

/**
 * Actualiza los campos de fecha según el tipo de período seleccionado
 */
function actualizarCamposFecha(tipo) {
    const camposFecha = document.getElementById('camposFecha');
    let html = '';

    switch (tipo) {
        case 'dia':
            html = `
                <div>
                    <label for="fechaDia" class="form-label">Fecha</label>
                    <input type="date" id="fechaDia" class="form-control">
                </div>
            `;
            break;

        case 'rango':
            html = `
                <div id="rangoFechas">
                    <div class="row g-2">
                        <div class="col-md-6">
                            <label for="fechaInicio" class="form-label">Fecha Inicio</label>
                            <input type="date" id="fechaInicio" class="form-control">
                        </div>
                        <div class="col-md-6">
                            <label for="fechaFin" class="form-label">Fecha Fin</label>
                            <input type="date" id="fechaFin" class="form-control">
                        </div>
                    </div>
                </div>
            `;
            break;

        case 'semana':
            html = `
                <div>
                    <label for="fechaSemana" class="form-label">Semana</label>
                    <input type="week" id="fechaSemana" class="form-control">
                </div>
            `;
            break;

        case 'mes':
            html = `
                <div>
                    <label for="fechaMes" class="form-label">Mes</label>
                    <input type="month" id="fechaMes" class="form-control">
                </div>
            `;
            break;

        case 'ano':
            html = `
                <div>
                    <label for="fechaAno" class="form-label">Año</label>
                    <input type="number" id="fechaAno" class="form-control" min="2000" max="2100" 
                           value="${new Date().getFullYear()}">
                </div>
            `;
            break;
    }

    camposFecha.innerHTML = html;

    // Reinicializar fechas si es rango
    if (tipo === 'rango') {
        inicializarFechas();
    }
}

/**
 * Obtiene las fechas de inicio y fin según el tipo de período
 */
function obtenerRangoFechas() {
    const tipo = document.getElementById('tipoPeriodo').value;
    let fechaInicio, fechaFin;

    switch (tipo) {
        case 'dia':
            const fechaDia = document.getElementById('fechaDia').value;
            if (!fechaDia) {
                alert('Por favor selecciona una fecha');
                return null;
            }
            fechaInicio = fechaDia;
            fechaFin = fechaDia;
            break;

        case 'rango':
            fechaInicio = document.getElementById('fechaInicio').value;
            fechaFin = document.getElementById('fechaFin').value;
            if (!fechaInicio || !fechaFin) {
                alert('Por favor selecciona ambas fechas');
                return null;
            }
            if (new Date(fechaInicio) > new Date(fechaFin)) {
                alert('La fecha de inicio no puede ser mayor que la fecha de fin');
                return null;
            }
            break;

        case 'semana':
            const semana = document.getElementById('fechaSemana').value;
            if (!semana) {
                alert('Por favor selecciona una semana');
                return null;
            }
            // Formato: 2024-W01
            const [anoSemana, sem] = semana.split('-W');
            const primerDia = obtenerPrimerDiaSemana(parseInt(anoSemana), parseInt(sem));
            const ultimoDia = new Date(primerDia);
            ultimoDia.setDate(ultimoDia.getDate() + 6);

            fechaInicio = formatearFecha(primerDia);
            fechaFin = formatearFecha(ultimoDia);
            break;

        case 'mes':
            const mes = document.getElementById('fechaMes').value;
            if (!mes) {
                alert('Por favor selecciona un mes');
                return null;
            }
            // Formato: 2024-01
            const [anoMes, numMes] = mes.split('-');
            const primerDiaMes = new Date(parseInt(anoMes), parseInt(numMes) - 1, 1);
            const ultimoDiaMes = new Date(parseInt(anoMes), parseInt(numMes), 0);

            fechaInicio = formatearFecha(primerDiaMes);
            fechaFin = formatearFecha(ultimoDiaMes);
            break;

        case 'ano':
            const anoCompleto = document.getElementById('fechaAno').value;
            if (!anoCompleto) {
                alert('Por favor ingresa un año');
                return null;
            }
            fechaInicio = `${anoCompleto}-01-01`;
            fechaFin = `${anoCompleto}-12-31`;
            break;
    }

    return { fechaInicio, fechaFin };
}

/**
 * Obtiene el primer día de una semana específica
 */
function obtenerPrimerDiaSemana(ano, semana) {
    const primerEnero = new Date(ano, 0, 1);
    const dias = (semana - 1) * 7;
    const resultado = new Date(primerEnero);
    resultado.setDate(primerEnero.getDate() + dias - primerEnero.getDay() + 1);
    return resultado;
}

/**
 * Formatea una fecha a YYYY-MM-DD
 */
function formatearFecha(fecha) {
    const ano = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${ano}-${mes}-${dia}`;
}

/**
 * Carga el análisis inicial (último mes)
 */
function cargarAnalisisInicial() {
    cargarAnalisis();
}

/**
 * Carga los datos de análisis
 */
function cargarAnalisis() {
    const rango = obtenerRangoFechas();
    if (!rango) return;

    cargarGanancias(rango.fechaInicio, rango.fechaFin);
    cargarProductosMasVendidos(rango.fechaInicio, rango.fechaFin);
}

/**
 * Carga el análisis de ganancias
 */
function cargarGanancias(fechaInicio, fechaFin) {
    const loading = document.getElementById('loadingGanancias');
    const contenido = document.getElementById('contenidoGanancias');

    console.log('Cargando ganancias para:', fechaInicio, 'a', fechaFin);

    loading.style.display = 'block';
    contenido.style.opacity = '0.5';

    fetch(`/analisis/ganancias?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`)
        .then(response => {
            console.log('Respuesta ganancias:', response.status, response.statusText);
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Error ${response.status}: ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos de ganancias recibidos:', data);
            actualizarMetricasGanancias(data);
            actualizarGraficoGanancias(data);
        })
        .catch(error => {
            console.error('Error al cargar ganancias:', error);
            alert('Error al cargar el análisis de ganancias: ' + error.message);
        })
        .finally(() => {
            loading.style.display = 'none';
            contenido.style.opacity = '1';
        });
}

/**
 * Actualiza las métricas de ganancias
 */
function actualizarMetricasGanancias(data) {
    document.getElementById('totalVentas').textContent = formatearMoneda(data.totalVentas);
    document.getElementById('totalCostos').textContent = formatearMoneda(data.totalCostos);
    document.getElementById('gananciaTotal').textContent = formatearMoneda(data.gananciaTotal);
    document.getElementById('porcentajeGanancia').textContent = data.porcentajeGanancia.toFixed(2) + '%';
}

/**
 * Actualiza el gráfico de ganancias
 */
function actualizarGraficoGanancias(data) {
    const ctx = document.getElementById('graficoGanancias').getContext('2d');

    // Destruir gráfico anterior si existe
    if (graficoGanancias) {
        graficoGanancias.destroy();
    }

    graficoGanancias = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Costos', 'Ganancia'],
            datasets: [{
                data: [data.totalCostos, data.gananciaTotal],
                backgroundColor: [
                    'rgba(255, 193, 7, 0.8)',
                    'rgba(25, 135, 84, 0.8)'
                ],
                borderColor: [
                    'rgba(255, 193, 7, 1)',
                    'rgba(25, 135, 84, 1)'
                ],
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                title: {
                    display: true,
                    text: 'Distribución de Costos y Ganancias'
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            const label = context.label || '';
                            const value = formatearMoneda(context.parsed);
                            const total = data.totalVentas;
                            const porcentaje = ((context.parsed / total) * 100).toFixed(2);
                            return `${label}: ${value} (${porcentaje}%)`;
                        }
                    }
                }
            }
        }
    });
}

/**
 * Carga los productos más vendidos
 */
function cargarProductosMasVendidos(fechaInicio, fechaFin, limite = 10) {
    const loading = document.getElementById('loadingProductos');
    const contenido = document.getElementById('contenidoProductos');

    console.log('Cargando productos más vendidos para:', fechaInicio, 'a', fechaFin);

    loading.style.display = 'block';
    contenido.style.opacity = '0.5';

    fetch(`/analisis/productos-mas-vendidos?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}&limite=${limite}`)
        .then(response => {
            console.log('Respuesta productos:', response.status, response.statusText);
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Error ${response.status}: ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos de productos recibidos:', data);
            actualizarTablaProductos(data);
            actualizarGraficoProductos(data);
        })
        .catch(error => {
            console.error('Error al cargar productos:', error);
            alert('Error al cargar los productos más vendidos: ' + error.message);
        })
        .finally(() => {
            loading.style.display = 'none';
            contenido.style.opacity = '1';
        });
}

/**
 * Actualiza la tabla de productos
 */
function actualizarTablaProductos(productos) {
    const tbody = document.getElementById('tablaProductos');

    if (productos.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center text-muted">
                    No hay ventas en el período seleccionado
                </td>
            </tr>
        `;
        return;
    }

    let html = '';
    productos.forEach((producto, index) => {
        html += `
            <tr>
                <td><strong>${index + 1}</strong></td>
                <td>${producto.nombreProducto}</td>
                <td class="text-end">${producto.cantidadVendida}</td>
                <td class="text-end">${formatearMoneda(producto.totalVentas)}</td>
            </tr>
        `;
    });

    tbody.innerHTML = html;
}

/**
 * Actualiza el gráfico de productos
 */
function actualizarGraficoProductos(productos) {
    const ctx = document.getElementById('graficoProductos').getContext('2d');

    // Destruir gráfico anterior si existe
    if (graficoProductos) {
        graficoProductos.destroy();
    }

    if (productos.length === 0) {
        return;
    }

    const labels = productos.map(p => p.nombreProducto);
    const cantidades = productos.map(p => p.cantidadVendida);

    graficoProductos = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Cantidad Vendida',
                data: cantidades,
                backgroundColor: 'rgba(13, 110, 253, 0.8)',
                borderColor: 'rgba(13, 110, 253, 1)',
                borderWidth: 2
            }]
        },
        options: {
            indexAxis: 'y',
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: 'Top 10 Productos Más Vendidos'
                }
            },
            scales: {
                x: {
                    beginAtZero: true,
                    ticks: {
                        precision: 0
                    }
                }
            }
        }
    });
}

/**
 * Formatea un número como moneda
 */
function formatearMoneda(valor) {
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
    }).format(valor || 0);
}
