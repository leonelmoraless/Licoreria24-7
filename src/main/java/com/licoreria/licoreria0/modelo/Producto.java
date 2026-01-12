
// sirve para los datos de los productos y mapear la tabla productos en la base de datos
package com.licoreria.licoreria0.modelo;

// mapea bd
import jakarta.persistence.*;

// sirve para que la base de datos reconozca esta clase como una tabla 
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_pk")
    private Long idProducto;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "precio_compra", nullable = false)
    private Double precioCompra;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "descuento")
    private Double descuento = 0.0;

    // Muchos productos pertenecen a UN proveedor
    // el fetch sirve para cargar automaticamente los datos del proveedor junto con
    // el producto
    // para filtrar los productos por proveedor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proveedor_fk", referencedColumnName = "id_proveedor_pk")
    private Proveedor proveedor;

    // constructor para que funcione la base de datos
    public Producto() {
    }

    // Constructor para la creacion manual por si se necesita
    public Producto(String nombre, Double precioCompra, Double precioVenta, Integer stock, Proveedor proveedor) {
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.proveedor = proveedor;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    // sirve para obtener el id del proveedor asociado al producto para el filtrado
    public Long getProveedorId() {
        if (this.proveedor != null) {
            return this.proveedor.getIdProveedor();
        } else {
            return null;
        }
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }
}