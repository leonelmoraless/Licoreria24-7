/*
Sirve para centralizar toda la lógica para que los controladores tengan menos trabajo
Gestiona Productos, Proveedores, Compras y maneja la Seguridad  
 */
package com.licoreria.licoreria0.patrones.facade;

import com.licoreria.licoreria0.modelo.*;
import com.licoreria.licoreria0.repositorio.*;
import com.licoreria.licoreria0.patrones.factory.ProductoFactory;
import com.licoreria.licoreria0.patrones.builder.VentaBuilder;
import com.licoreria.licoreria0.patrones.strategy.ContextoPago;
import com.licoreria.licoreria0.patrones.strategy.MetodoPagoStrategy;
import com.licoreria.licoreria0.patrones.observer.ObservadorVenta;
import com.licoreria.licoreria0.servicio.ServicioEmail;

// Importaciones del spring para componentes, transacciones y seguridad
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@Component
public class Facade {

    // hace que Spring inyecte automáticamente las dependencias
    @Autowired
    private ProveedorRepositorio proveedorRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private CompraRepositorio compraRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private VentaRepositorio ventaRepositorio;

    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;

    @Autowired
    private PagoRepositorio pagoRepositorio;

    // esta dependencia es para el patron factory y el servicio de email

    @Autowired
    private ProductoFactory productoFactory;

    @Autowired
    private DetalleCompraRepositorio detalleCompraRepositorio;

    @Autowired
    private PasswordEncoder codificadorDeContrasena;

    @Autowired
    private ServicioEmail servicioEmail;

    // --- NUEVAS DEPENDENCIAS PARA PATRONES DE DISEÑO ---
    @Autowired
    private ContextoPago contextoPago;

    @Autowired
    private List<ObservadorVenta> observadoresVentas;

    // gestiona los proveedores verificando duplicados y validando datos
    public Proveedor registrarProveedor(Proveedor proveedor) throws Exception {

        String rucDelProveedor = proveedor.getRuc();

        if (rucDelProveedor != null) {
            if (!rucDelProveedor.isEmpty()) {

                Optional<Proveedor> posibleDuplicado = proveedorRepositorio.findByRuc(rucDelProveedor);

                if (posibleDuplicado.isPresent()) {
                    throw new Exception("El RUC " + rucDelProveedor + " ya está registrado.");
                }
            }
        }

        Proveedor proveedorGuardado = proveedorRepositorio.save(proveedor);
        return proveedorGuardado;
    }

    /**
     * Actualiza los datos del proveedor siempre validando que el nuevo RUC no esté
     * en uso
     */
    public Proveedor actualizarProveedor(Proveedor proveedorConNuevosDatos) throws Exception {

        Long idProveedor = proveedorConNuevosDatos.getIdProveedor();
        Proveedor proveedorOriginal = proveedorRepositorio.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado."));

        String nuevoRuc = proveedorConNuevosDatos.getRuc();
        String rucOriginal = proveedorOriginal.getRuc();

        if (nuevoRuc != null) {
            if (!nuevoRuc.isEmpty()) {
                if (!nuevoRuc.equals(rucOriginal)) {

                    if (proveedorRepositorio.findByRuc(nuevoRuc).isPresent()) {
                        throw new Exception("El nuevo RUC " + nuevoRuc + " ya está en uso.");
                    }
                }
            }
        }

        // Actualizar y guarda los datos a la base de datos
        proveedorOriginal.setNombre(proveedorConNuevosDatos.getNombre());
        proveedorOriginal.setRuc(proveedorConNuevosDatos.getRuc());
        proveedorOriginal.setTelefono(proveedorConNuevosDatos.getTelefono());
        proveedorOriginal.setDireccion(proveedorConNuevosDatos.getDireccion());
        proveedorOriginal.setCorreo(proveedorConNuevosDatos.getCorreo());

        Proveedor proveedorActualizado = proveedorRepositorio.save(proveedorOriginal);
        return proveedorActualizado;
    }

    /**
     * retorna todos los proveedores
     */
    public List<Proveedor> obtenerTodosProveedores() {
        List<Proveedor> listaDeProveedores = proveedorRepositorio.findAll();
        return listaDeProveedores;
    }

    /**
     * Elimina proveedor solo si no tiene productos asociados
     */
    public void eliminarProveedor(Long idProveedor) throws Exception {

        Proveedor proveedor = proveedorRepositorio.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado."));
        List<Producto> productosDelProveedor = proveedor.getProductos();

        if (productosDelProveedor != null) {
            if (!productosDelProveedor.isEmpty()) {
                int cantidadProductos = productosDelProveedor.size();
                throw new Exception(
                        "No se puede eliminar. El proveedor tiene " + cantidadProductos + " productos asociados.");
            }
        }

        // Verificar si tiene compras asociadas
        if (compraRepositorio.existsByProveedor_IdProveedor(idProveedor)) {
            throw new Exception("No se puede eliminar. El proveedor tiene compras registradas.");
        }
        proveedorRepositorio.deleteById(idProveedor);
    }

    // gestiona los productos validando datos y usando el patron factory
    public Producto registrarProducto(Producto producto, Long idProveedor) throws Exception {

        Proveedor proveedorEncontrado = proveedorRepositorio.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado con ID: " + idProveedor));

        String nombre = producto.getNombre();
        Double precioCompra = producto.getPrecioCompra();
        Double precioVenta = producto.getPrecioVenta();
        Integer stock = producto.getStock();

        Producto nuevoProducto = productoFactory.crearProducto(
                nombre,
                precioCompra,
                precioVenta,
                stock,
                proveedorEncontrado);

        // Guardar en la base de datos
        Producto productoGuardado = productoRepositorio.save(nuevoProducto);
        return productoGuardado;
    }

    /**
     * Actualiza los datos de un producto
     */
    public Producto actualizarProducto(Producto productoConDatosNuevos, Long idProveedor) throws Exception {

        Long idProducto = productoConDatosNuevos.getIdProducto();
        Producto productoOriginal = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado."));
        Proveedor proveedor = proveedorRepositorio.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado con ID: " + idProveedor));

        productoOriginal.setNombre(productoConDatosNuevos.getNombre());
        productoOriginal.setPrecioCompra(productoConDatosNuevos.getPrecioCompra());
        productoOriginal.setPrecioVenta(productoConDatosNuevos.getPrecioVenta());
        productoOriginal.setStock(productoConDatosNuevos.getStock());
        productoOriginal.setProveedor(proveedor);

        Producto productoActualizado = productoRepositorio.save(productoOriginal);
        return productoActualizado;
    }

    /**
     * retorna todos los productos registrados
     */
    public List<Producto> obtenerTodosProductos() {
        return productoRepositorio.findAll();
    }

    /**
     * Elimina un producto pero valida que su stock este vacio
     */
    public void eliminarProducto(Long idProducto) throws Exception {

        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado."));

        // Verificar si existen compras asociadas a este producto
        if (detalleCompraRepositorio.existsByProducto_IdProducto(idProducto)) {
            throw new Exception("No se puede eliminar el producto porque existe una compra con este producto.");
        }

        // Verificar stock
        Integer stockActual = producto.getStock();
        if (stockActual > 0) {
            throw new Exception("No se puede eliminar un producto con stock (" + stockActual + ").");
        }

        // Eliminar
        productoRepositorio.deleteById(idProducto);
    }

    /**
     * Actualiza el stock de un producto
     */
    public Producto actualizarStockProducto(Long idProducto, int cantidadParaAgregar) throws Exception {

        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado para actualizar stock: " + idProducto));

        int stockActual = producto.getStock();
        int nuevoStock = stockActual + cantidadParaAgregar;

        // no permitir stock negativo
        if (nuevoStock < 0) {
            throw new Exception("Stock insuficiente para " + producto.getNombre() + ". Stock actual: " + stockActual);
        }

        producto.setStock(nuevoStock);
        Producto productoActualizado = productoRepositorio.save(producto);
        return productoActualizado;
    }

    // gestiona las compras validando datos y actualizando stock de productos
    // usa excepciones para manejar errores

    @Transactional
    public Compra registrarCompra(CompraPeticionDTO peticionDeCompra) throws Exception {

        Long idProveedor = peticionDeCompra.getIdProveedor();
        Proveedor proveedor = proveedorRepositorio.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado."));

        // crea objetos de compra y detalle para guardar en la base de datos
        Compra nuevaCompra = new Compra();
        nuevaCompra.setProveedor(proveedor);
        nuevaCompra.setFecha(new Date());

        double totalDeLaCompra = 0.0;
        List<DetalleCompra> listaDeDetalles = new ArrayList<>();
        List<CompraPeticionDTO.ItemCompraDTO> items = peticionDeCompra.getItems();
        // busca cada producto, calcula subtotales y actualiza stock
        for (CompraPeticionDTO.ItemCompraDTO item : items) {
            Long idProducto = item.getIdProducto();
            Producto producto = productoRepositorio.findById(idProducto)
                    .orElseThrow(() -> new Exception("Producto no encontrado: " + idProducto));

            Double precioUnitario = item.getPrecioUnitario();
            Integer cantidad = item.getCantidad();
            double subtotal = precioUnitario * cantidad;

            totalDeLaCompra = totalDeLaCompra + subtotal;

            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(nuevaCompra);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            listaDeDetalles.add(detalle);

            this.actualizarStockProducto(idProducto, cantidad);
        }

        // envia a la compra el total y los detalles calculados
        nuevaCompra.setTotal(totalDeLaCompra);
        nuevaCompra.setDetalles(listaDeDetalles);

        Compra compraGuardada = compraRepositorio.save(nuevaCompra);

        System.out.println("Facade: Compra " + compraGuardada.getIdCompra() + " registrada con éxito.");
        return compraGuardada;
    }

    /**
     * retorna todas las compras registradas
     */
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepositorio.findAll();
    }

    // elimina una compra actualizando el stock de los productos
    @Transactional
    public void eliminarCompra(Long idCompra) throws Exception {
        Compra compra = compraRepositorio.findById(idCompra)
                .orElseThrow(() -> new Exception("Compra no encontrada."));

        List<DetalleCompra> detalles = compra.getDetalles();
        for (DetalleCompra detalle : detalles) {
            Long idProducto = detalle.getProducto().getIdProducto();
            Integer cantidadComprada = detalle.getCantidad();

            // Verificar si el producto aún existe antes de actualizar el stock
            if (productoRepositorio.findById(idProducto).isPresent()) {
                // restaurar el stock del producto
                actualizarStockProducto(idProducto, -cantidadComprada);
            }
            // Si el producto ya no existe, simplemente lo ignoramos
        }

        compraRepositorio.delete(compra);
    }

    // Esta es la parte del facade que gestiona los usuarios y la seguridad
    // registra, actualiza, elimina, cambia y recupera contraseñas
    public Usuario registrarUsuario(Usuario nuevoUsuario) throws Exception {
        String correo = nuevoUsuario.getCorreo();
        if (usuarioRepositorio.findByCorreo(correo).isPresent()) {
            throw new Exception("El correo " + correo + " ya está en uso.");
        }

        // Aqui encripta la contraseña antes de guardar
        String contrasenaPlana = nuevoUsuario.getContrasena();
        String contrasenaEncriptada = codificadorDeContrasena.encode(contrasenaPlana);
        nuevoUsuario.setContrasena(contrasenaEncriptada);

        Usuario usuarioGuardado = usuarioRepositorio.save(nuevoUsuario);
        return usuarioGuardado;
    }

    // Actualiza los datos de un usuario validando cambios de correo y contraseña
    // tambien usa excepciones para manejar errores
    public Usuario actualizarUsuario(Usuario usuarioConNuevosDatos) throws Exception {

        Long idUsuario = usuarioConNuevosDatos.getIdUsuario();
        if (idUsuario == null) {
            throw new Exception("El ID del usuario no puede ser nulo al actualizar.");
        }

        Usuario usuarioOriginal = usuarioRepositorio.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        String nuevoCorreo = usuarioConNuevosDatos.getCorreo();
        String correoOriginal = usuarioOriginal.getCorreo();

        if (!nuevoCorreo.equals(correoOriginal)) {
            if (usuarioRepositorio.findByCorreo(nuevoCorreo).isPresent()) {
                throw new Exception("El nuevo correo " + nuevoCorreo + " ya está en uso.");
            }
        }
        // actualiza la contraseña y si no se cambia la deja igual
        String nuevaContrasena = usuarioConNuevosDatos.getContrasena();

        if (nuevaContrasena != null) {
            if (!nuevaContrasena.isEmpty()) {
                String contrasenaEncriptada = codificadorDeContrasena.encode(nuevaContrasena);
                usuarioOriginal.setContrasena(contrasenaEncriptada);
            }

        }

        // actualiza otros datos y guarda
        usuarioOriginal.setNombre(usuarioConNuevosDatos.getNombre());
        usuarioOriginal.setCorreo(nuevoCorreo);
        usuarioOriginal.setRol(usuarioConNuevosDatos.getRol());

        Usuario usuarioActualizado = usuarioRepositorio.save(usuarioOriginal);
        return usuarioActualizado;
    }

    // esto permite que al iniciar sesion, el usuario puede cambiar su contraseña
    // pide la actual y 2 veces la nueva para confirmarla
    public void cambiarContrasena(String contrasenaActual, String nuevaContrasena, String confirmarContrasena)
            throws Exception {
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            throw new Exception("Las nuevas contraseñas no coinciden.");
        }

        String correoUsuarioLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepositorio.findByCorreo(correoUsuarioLogueado)
                .orElseThrow(() -> new Exception("Usuario no encontrado en la sesión."));

        String contrasenaGuardada = usuario.getContrasena();
        // si todo esta bien actualiza la contraseña y se guarda
        if (codificadorDeContrasena.matches(contrasenaActual, contrasenaGuardada)) {
            String nuevaEncriptada = codificadorDeContrasena.encode(nuevaContrasena);
            usuario.setContrasena(nuevaEncriptada);
            usuarioRepositorio.save(usuario);
        } else {
            throw new Exception("La contraseña actual es incorrecta.");
        }
    }

    // este es el metod que permite recuperar la contraseña por correo
    public void recuperarContrasena(String correoDestino) {
        Optional<Usuario> usuarioOpcional = usuarioRepositorio.findByCorreo(correoDestino);

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            // genera una contraseña de 8 caracteres
            String nuevaClaveTemporal = UUID.randomUUID().toString().substring(0, 8);

            // guarda la nueva clave encriptada
            usuario.setContrasena(codificadorDeContrasena.encode(nuevaClaveTemporal));
            usuarioRepositorio.save(usuario);

            // Este es el contenido del correo que se envia
            String asunto = "Recuperación de Contraseña - Licorería 24/7";
            String nombreUsuario = usuario.getNombre();

            String contenidoHtml = """
                    <html>
                      <body style='font-family: Arial, sans-serif; color:#222;'>
                        <p>Hola %s,</p>
                        <p>Has solicitado recuperar tu contraseña...</p>
                        <p><strong>Tu nueva contraseña es:</strong> %s</p>
                        <p>Corre a iniciar sesión y cámbiala lo antes posible</p>
                        <p style='margin-top:20px;'>
                          <img src='cid:fotoadjunta' alt='Licorería 24/7' style='max-width:200px;'>
                        </p>
                        <p>Saludos <br>El equipo de Licorería 24/7 </p>
                      </body>
                    </html>
                    """.formatted(nombreUsuario, nuevaClaveTemporal);

            // envia correo con la nueva contraseña
            servicioEmail.enviarCorreoHtmlConInline(
                    correoDestino,
                    asunto,
                    contenidoHtml,
                    "fotoadjunta",
                    "static/imagenes/todosloscaminos.jpg",
                    "image/jpeg");
        }
    }

    // busca un usuario por su correo para login
    public Optional<Usuario> buscarUsuarioParaLogin(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    // retorna todos los usuarios registrados
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    // elimina un usuario validando que no se elimine a si mismo
    public void eliminarUsuario(Long idUsuarioEliminar) throws Exception {
        Usuario usuario = usuarioRepositorio.findById(idUsuarioEliminar)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // aqui es donde valida que no se elimine a si mismo
        String correoUsuarioLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        String correoUsuarioEliminar = usuario.getCorreo();

        if (correoUsuarioEliminar.equals(correoUsuarioLogueado)) {
            throw new Exception("No puede eliminarse a sí mismo bobo.");
        }

        usuarioRepositorio.deleteById(idUsuarioEliminar);
    }

    // Setter para inyección manual en tests
    public void setProductoFactory(ProductoFactory productoFactory) {
        this.productoFactory = productoFactory;
    }

    // --------------------------------------------------------------------------------------
    // GESTIÓN DE CLIENTES
    // --------------------------------------------------------------------------------------

    public Cliente registrarCliente(Cliente cliente) throws Exception {
        // Validar si la cédula ya existe
        if (cliente.getCedula() != null && !cliente.getCedula().isEmpty()) {
            if (clienteRepositorio.findByCedula(cliente.getCedula()).isPresent()) {
                throw new Exception("La cédula " + cliente.getCedula() + " ya está registrada.");
            }
        }
        return clienteRepositorio.save(cliente);
    }

    public List<Cliente> obtenerTodosClientes() {
        return clienteRepositorio.findAll();
    }

    public void actualizarCliente(Cliente cliente) throws Exception {
        if (!clienteRepositorio.existsById(cliente.getIdCliente())) {
            throw new Exception("Cliente no encontrado.");
        }
        clienteRepositorio.save(cliente);
    }

    public void eliminarCliente(Long idCliente) throws Exception {
        if (ventaRepositorio.existsByCliente_IdCliente(idCliente)) {
            throw new Exception("No se puede eliminar. El cliente tiene ventas registradas.");
        }
        clienteRepositorio.deleteById(idCliente);
    }

    // --------------------------------------------------------------------------------------
    // GESTIÓN DE VENTAS
    // --------------------------------------------------------------------------------------

    @Transactional(rollbackFor = Exception.class) // Importante: Se asegura que rollback en cualquier excepcion
                                                  // (incluidas checked)
    public Venta registrarVenta(VentaPeticionDTO peticionVenta) throws Exception {

        // 1. Obtener Cliente
        Long idCliente = peticionVenta.getIdCliente();
        Cliente cliente = clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new Exception("Cliente no encontrado con ID: " + idCliente));

        // 2. Usar BUILDER para construir la venta compleja
        VentaBuilder builder = new VentaBuilder();
        builder.conCliente(cliente);

        List<VentaPeticionDTO.ItemVentaDTO> items = peticionVenta.getItems();
        for (VentaPeticionDTO.ItemVentaDTO item : items) {
            Long idProducto = item.getIdProducto();
            Producto producto = productoRepositorio.findById(idProducto)
                    .orElseThrow(() -> new Exception("Producto no encontrado: " + idProducto));

            // El builder valida stock (Fail Fast) y calcula subtotales
            builder.agregarDetalle(producto, item.getCantidad(), item.getPrecioUnitario());
        }

        Venta nuevaVenta = builder.construir();

        // 3. Guardar Venta (Cascade salvará detalles)
        Venta ventaGuardada = ventaRepositorio.save(nuevaVenta);

        // 4. Procesar Pago usando STRATEGY
        String metodoNombre = peticionVenta.getMetodoPago();
        MetodoPagoStrategy estrategia = contextoPago.obtenerEstrategia(metodoNombre);
        estrategia.procesarPago(ventaGuardada, ventaGuardada.getTotal());

        // Guardamos el registro del pago simple para historial
        if (metodoNombre != null && !metodoNombre.isEmpty()) {
            Pago pago = new Pago(ventaGuardada, metodoNombre);
            pagoRepositorio.save(pago);
        }

        // 5. Notificar a OBSERVERS (Actualizar Inventario)
        for (ObservadorVenta observador : observadoresVentas) {
            observador.notificarVenta(ventaGuardada);
        }

        return ventaGuardada;
    }

    public List<Venta> obtenerTodasVentas() {
        return ventaRepositorio.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminarVenta(Long idVenta) throws Exception {
        Venta venta = ventaRepositorio.findById(idVenta)
                .orElseThrow(() -> new Exception("Venta no encontrada: " + idVenta));

        // Restaurar Stock
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            // Sumamos la cantidad vendida de nuevo al stock
            actualizarStockProducto(producto.getIdProducto(), detalle.getCantidad());
        }

        // Eliminar Venta (Cascade eliminará detalles y pagos)
        ventaRepositorio.delete(venta);
    }
}
