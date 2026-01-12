const email = document.querySelector('#username') || document.querySelector('#correo');
const password = document.querySelector('#password');
const boton = document.querySelector('.boton');
const noticia = document.querySelector('.noticia');

// Validación simple de campos para habilitar/deshabilitar botón
if (boton) {
    // Si estamos en login (hay email y password), validamos
    if (email && password) {
        validarLogin(); // Validar estado inicial
        email.addEventListener('input', validarLogin);
        password.addEventListener('input', validarLogin);
    }
    // Si solo hay email (recuperar contraseña)
    else if (email) {
        validarSoloCorreo();
        email.addEventListener('input', validarSoloCorreo);
    }
}

function validarLogin() {
    if (!email || !password) return;
    const vacio = email.value.trim() === '' || password.value.trim() === '';
    boton.disabled = vacio;
}

function validarSoloCorreo() {
    if (!email) return;
    const vacio = email.value.trim() === '';
    boton.disabled = vacio;
}


// ------------------------------------------------------------------------------------------------
// APLICAR ESTADO DEL SIDEBAR INMEDIATAMENTE (Antes de que cargue la página)
// ------------------------------------------------------------------------------------------------
(function () {
    const sidebar = document.querySelector('.sidebar');
    const savedState = localStorage.getItem('sidebarCollapsed');
    if (sidebar && savedState === 'true') {
        sidebar.classList.add('collapsed');
    }
})();

// ------------------------------------------------------------------------------------------------
// LOADER SOLAMENTE PARA LOGIN
// ------------------------------------------------------------------------------------------------
function initLoader() {

    const loader = document.getElementById('loader');
    const loaderText = loader ? loader.querySelector('h3') : null;

    // --- FUNCIÓN ACTIVAR LOADER ---
    window.activarLoader = function () {
        if (!loader) return;
        loader.style.display = 'flex';
        if (loaderText) loaderText.textContent = "Ingresando...";
    }

    window.desactivarLoader = function () {
        if (!loader) return;
        loader.style.display = 'none';
        if (loaderText) loaderText.textContent = "Cargando...";
    }

    // --- SIDEBAR TOGGLE WITH TOOLTIPS AND PERSISTENCE ---
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');
    let tooltipList = [];

    function initTooltips() {
        tooltipList.forEach(tooltip => tooltip.dispose());
        tooltipList = [];
        if (sidebar && sidebar.classList.contains('collapsed')) {
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl =>
                new bootstrap.Tooltip(tooltipTriggerEl, {
                    trigger: 'hover',
                    delay: { show: 300, hide: 100 }
                })
            );
        }
    }

    // Highlight active page based on current URL
    function highlightActivePage() {
        const currentPath = window.location.pathname;
        const navLinks = document.querySelectorAll('.sidebar .nav-link');

        navLinks.forEach(link => {
            const linkPath = new URL(link.href).pathname;
            if (linkPath === currentPath) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });
    }

    if (toggleBtn && sidebar) {
        initTooltips();
        highlightActivePage();

        toggleBtn.addEventListener('click', (e) => {
            e.preventDefault();
            sidebar.classList.toggle('collapsed');
            document.documentElement.classList.toggle('sidebar-collapsed-state');
            const isCollapsed = sidebar.classList.contains('collapsed');
            localStorage.setItem('sidebarCollapsed', isCollapsed);
            setTimeout(() => initTooltips(), 350);
        });
    }

    // --- LOADER SOLO EN FORMULARIO DE LOGIN ---
    const loginForm = document.querySelector('form[action*="login"]');
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            // Activar loader solo al hacer submit en login
            window.activarLoader();
            // No prevenimos el submit por defecto para que Spring Security maneje la redirección,
            // pero el loader se mostrará visualmente hasta que la página cambie.
        });
    }

    // Si hay parámetros de error en la URL (login fallido), aseguramos que el loader esté oculto
    // y hacemos un pequeño shake visual si es login
    if (new URLSearchParams(window.location.search).has('error')) {
        if (loginForm) {
            document.body.style.animation = 'shake 0.5s';
            document.body.style.color = 'red';
            setTimeout(() => {
                document.body.style.animation = '';
                document.body.style.color = '';
            }, 500);
        }
        window.desactivarLoader();
    }

    window.addEventListener('pageshow', (event) => {
        if (event.persisted) window.desactivarLoader();
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initLoader);
} else {
    initLoader();
}

if (boton) {
    boton.disabled = true;
    boton.addEventListener('mouseover', movimientoBoton);
    boton.addEventListener('touchstart', movimientoBoton);
}

const frasesTroll = [
    "Te cobro por letra??",
    "Atrápame si puedes ",
    "Sin datos no hay trago ",
    "Miedo al éxito? Llena los campos",
    "Demuéstra que estás sobrio",
    "Te falta info, crack",
    "El teclado no muerde ",
    "Soy más rápido que tú ",
    "Qué mirás bobo? Andá pa' allá",
    "Llena los campos, no seas MIGAJERO",
    "Por eso te dejó :(",
    "Falta data, igual que el interés de tu ex",
    "Todos los caminos llevan a ROMA",
    "Si no llenas esto, no hay trago para olvidarla",
    "Escribe bien, que tu hígado aguanta más que tu corazón",
    "No mames, que el teclado no muerde",
    "Aquí no fiamos, ni volvemos con ex. Escribe!",
    "El bicho no estaria orgulloso bro",
    "Con esta la pones bro"
];

function movimientoBoton() {
    if (boton && boton.disabled) {
        const indice = Math.floor(Math.random() * frasesTroll.length);
        noticia.innerHTML = frasesTroll[indice];
        noticia.style.color = 'rgb(218, 49, 49)';
        noticia.style.fontWeight = 'bold';

        const x = Math.random() * 300 - 150;
        const y = Math.random() * 300 - 150;
        boton.style.transform = `translate(${x}px, ${y}px)`;
    }
}

function validarLogin() {
    if (!email || !password) return;
    const vacio = email.value.trim() === '' || password.value.trim() === '';
    vacio ? bloquearBoton() : desbloquearBoton('<b>Puedes entrar crack</b>');
}

function validarSoloCorreo() {
    if (!email) return;
    const vacio = email.value.trim() === '';
    vacio ? bloquearBoton() : desbloquearBoton('<b>No te preocupes crack</b>');
}

function bloquearBoton() {
    if (boton) {
        boton.disabled = true;
        boton.classList.remove('stay');
    }
}

function desbloquearBoton(mensajeExito) {
    if (boton) {
        boton.disabled = false;
        boton.style.transform = 'translate(0px, 0px)';
        noticia.innerHTML = mensajeExito;
        noticia.style.color = '#198754';
        boton.classList.add('stay');
    }
}

if (password && email) {
    bloquearBoton();
    email.addEventListener('input', validarLogin);
    password.addEventListener('input', validarLogin);
} else if (email) {
    bloquearBoton();
    email.addEventListener('input', validarSoloCorreo);
}

// ------------------------------------------------------------------------------------------------
// APLICAR ESTADO DEL SIDEBAR INMEDIATAMENTE (Antes de que cargue la página)
// ------------------------------------------------------------------------------------------------
// Esto previene el "flash" de expansión/contracción al navegar rápido
(function () {
    const sidebar = document.querySelector('.sidebar');
    const savedState = localStorage.getItem('sidebarCollapsed');
    if (sidebar && savedState === 'true') {
        sidebar.classList.add('collapsed');
    }
})();

// ------------------------------------------------------------------------------------------------
// LOADER INTELIGENTE (FAIL FAST, SUCCESS SLOW) Y EFECTO SHAKE
// ------------------------------------------------------------------------------------------------
// Se encapsula en una función 'initLoader' que se ejecuta cuando el DOM está listo.
function initLoader() {

    const loader = document.getElementById('loader');
    const loaderText = loader ? loader.querySelector('h3') : null;
    let messageInterval;

    const mensajes = [
        "Cargando...",
        "Validando datos...",
        "Hackeando la UTM...",
        "Desencriptando contraseñas...",
        "Verificando edad...",
    ];

    // --- FUNCIÓN ACTIVAR LOADER ---
    // La exponemos a window para depuración si es necesario
    window.activarLoader = function (modoTroll = false) {
        if (!loader) return;

        clearInterval(messageInterval);
        loader.style.display = 'flex';

        if (modoTroll) {
            let i = 0;
            if (loaderText) loaderText.textContent = mensajes[0];
            messageInterval = setInterval(() => {
                i = (i + 1) % mensajes.length;
                if (loaderText) loaderText.textContent = mensajes[i];
            }, 1500);
        } else {
            if (loaderText) loaderText.textContent = "Cargando...";
        }
    }

    window.desactivarLoader = function () {
        if (!loader) return;
        loader.style.display = 'none';
        clearInterval(messageInterval);
        if (loaderText) loaderText.textContent = "Cargando...";
    }

    function errorVisual() {
        document.body.style.animation = 'shake 0.5s';
        document.body.style.color = 'red';
        setTimeout(() => {
            document.body.style.animation = '';
            document.body.style.color = '';
        }, 500);
    }

    // Detectar error desde backend (?error)
    if (new URLSearchParams(window.location.search).has('error')) {
        errorVisual();
    }

    // --- INTERCEPCIÓN DE CLICS EN ENLACES (Navegación) ---
    // Usamos Delegación de Eventos para detectar clics en cualquier parte del documento
    document.addEventListener('click', function (e) {
        const link = e.target.closest('a');

        // Filtros para ignorar clics que no son navegación real (modales, js, targets externos)
        if (!link || link.target === '_blank' || link.getAttribute('href')?.startsWith('#') || link.getAttribute('href')?.startsWith('javascript') || link.hasAttribute('data-bs-toggle')) {
            return;
        }

        const href = link.getAttribute('href');

        // Si es una URL válida:
        if (href && href !== '#' && href.trim() !== '') {
            e.preventDefault(); // 1. Detenemos la navegación inmediata

            // 2. Mostramos el loader visualmente
            if (loader) loader.style.display = 'flex';
            if (loaderText) loaderText.textContent = "Cargando...";

            // 3. Esperamos 1 segundo para que la animación se disfrute, luego navegamos
            setTimeout(() => {
                window.location.href = href;
            }, 400);
        }
    });

    // --- FUNCIÓN GLOBAL PARA MOSTRAR TICKET UNIVERSAL ---
    window.mostrarTicketUniversal = function (dto, callbackCierre) {
        // 1. Llenar datos
        document.getElementById('ticketIdUniversal').textContent = dto.idVenta;
        document.getElementById('ticketClienteUniversal').textContent = dto.nombreCliente;
        document.getElementById('ticketFechaUniversal').textContent = dto.fecha;
        // El DTO puede traer String formateado o number (del backend viene number usualmente)
        // Aseguramos formato $
        let totalStr = dto.total;
        if (typeof dto.total === 'number') {
            totalStr = "$" + dto.total.toFixed(2);
        } else if (!String(dto.total).includes('$')) {
            totalStr = "$" + dto.total;
        }
        document.getElementById('ticketTotalUniversal').textContent = totalStr;

        // 2. Llenar tabla
        const tbody = document.getElementById('ticketItemsUniversal');
        tbody.innerHTML = '';

        dto.items.forEach(item => {
            const precioUnit = (typeof item.precioUnitario === 'number') ? item.precioUnitario.toFixed(2) : item.precioUnitario;
            const subtotal = (typeof item.subtotal === 'number') ? item.subtotal.toFixed(2) : item.subtotal;

            let fila = `
                <tr>
                    <td>${item.producto}</td>
                    <td>${item.cantidad}</td>
                    <td>$${precioUnit}</td>
                    <td>$${subtotal}</td>
                </tr>
            `;
            tbody.innerHTML += fila;
        });

        // 3. Configurar comportamiento al cerrar
        const btnCerrar = document.getElementById('btnCerrarTicketUniversal');
        // Clonamos el botón para quitar listeners anteriores y evitar acumulaciones
        const nuevoBtn = btnCerrar.cloneNode(true);
        btnCerrar.parentNode.replaceChild(nuevoBtn, btnCerrar);

        nuevoBtn.addEventListener('click', function () {
            // Cerrar el modal bootstrap
            // (Ya lo hace data-bs-dismiss, pero si hay callback extra...)
            if (callbackCierre) {
                callbackCierre();
            }
        });

        // 4. Mostrar Modal
        const modalEl = document.getElementById('modalTicketUniversal');
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    };


    // --- SIDEBAR TOGGLE WITH TOOLTIPS AND PERSISTENCE ---
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');

    // Initialize Bootstrap tooltips
    let tooltipList = [];

    function initTooltips() {
        // Dispose existing tooltips
        tooltipList.forEach(tooltip => tooltip.dispose());
        tooltipList = [];

        // Only initialize tooltips when sidebar is collapsed
        if (sidebar && sidebar.classList.contains('collapsed')) {
            const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
            tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl =>
                new bootstrap.Tooltip(tooltipTriggerEl, {
                    trigger: 'hover',
                    delay: { show: 300, hide: 100 }
                })
            );
        }
    }

    // Highlight active page based on current URL
    function highlightActivePage() {
        const currentPath = window.location.pathname;
        const navLinks = document.querySelectorAll('.sidebar .nav-link');

        navLinks.forEach(link => {
            const linkPath = new URL(link.href).pathname;
            if (linkPath === currentPath) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });
    }

    if (toggleBtn && sidebar) {
        // Initialize tooltips and highlight active page on load
        initTooltips();
        highlightActivePage();

        toggleBtn.addEventListener('click', (e) => {
            e.preventDefault();
            sidebar.classList.toggle('collapsed');
            document.documentElement.classList.toggle('sidebar-collapsed-state'); // Sincronizar para futuras cargas

            // Save state to localStorage
            const isCollapsed = sidebar.classList.contains('collapsed');
            localStorage.setItem('sidebarCollapsed', isCollapsed);

            // Reinitialize tooltips based on new state
            setTimeout(() => initTooltips(), 350); // Wait for transition
        });
    }

    // --- THEME TOGGLE LOGIC ---
    const themeBtn = document.getElementById('themeToggle');
    const themeIcon = document.getElementById('themeIcon');

    // Function to update icon
    const updateThemeIcon = (isLight) => {
        if (!themeIcon) return;
        if (isLight) {
            themeIcon.classList.remove('bi-moon-stars-fill');
            themeIcon.classList.add('bi-sun-fill');
            themeBtn.style.color = "#ffc107"; // Yellow sun
        } else {
            themeIcon.classList.remove('bi-sun-fill');
            themeIcon.classList.add('bi-moon-stars-fill');
            themeBtn.style.color = "var(--text-muted)";
        }
    }

    // Init Icon based on current state
    if (document.documentElement.classList.contains('light-theme')) {
        updateThemeIcon(true);
    }

    if (themeBtn) {
        themeBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const isLight = document.documentElement.classList.toggle('light-theme');
            localStorage.setItem('theme', isLight ? 'light' : 'dark');
            updateThemeIcon(isLight);
        });
    }


    // --- LOADER EN FORMULARIOS ---
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) return;
            event.preventDefault();

            const esLogin = form.action.includes('login');
            window.activarLoader(esLogin);

            const startTime = Date.now();
            const params = new URLSearchParams(new FormData(form));

            fetch(form.action, {
                method: 'POST',
                body: params,
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
            })
                .then(response => {
                    const errorEnUrl = response.url.includes('error');
                    // Check logic: if response was redirect (ok) but has error param, OR if response not ok
                    if (errorEnUrl || !response.ok) {
                        // Force reloading current page to show error message (if backend redirected with error param)
                        // Or if it's staying on page, deactive loader
                        window.location.href = response.url;
                        // Note: The loader init logic on new page load attempts to handle 'error' param
                    } else {
                        const elapsed = Date.now() - startTime;
                        const tiempoEspera = esLogin ? 7500 : 500;
                        const remaining = Math.max(0, tiempoEspera - elapsed);
                        setTimeout(() => window.location.href = response.url, remaining);
                    }
                })
                .catch(() => {
                    window.desactivarLoader();
                    errorVisual();
                });
        });
    });

    window.addEventListener('pageshow', (event) => {
        if (event.persisted) window.desactivarLoader();
    });
}

// Ejecutar initLoader cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initLoader);
} else {
    initLoader();
}