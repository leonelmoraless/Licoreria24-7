const email = document.querySelector('#username') || document.querySelector('#correo');
const password = document.querySelector('#password');
const boton = document.querySelector('.boton');
const noticia = document.querySelector('.noticia');

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

    // --- SIDEBAR TOGGLE ---
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.sidebar');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', (e) => {
            e.preventDefault(); // Evitar comportamientos extraños
            sidebar.classList.toggle('collapsed');
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
                    if (response.url.includes('error')) {
                        window.desactivarLoader();
                        errorVisual();
                    } else {
                        const elapsed = Date.now() - startTime;
                        const tiempoEspera = esLogin ? 7500 : 500;
                        const remaining = Math.max(0, tiempoEspera - elapsed);
                        setTimeout(() => window.location.href = response.url, remaining);
                    }
                })
                .catch(() => window.desactivarLoader());
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