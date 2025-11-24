
// sirve para los datos de los proveedores y mapear la tabla proveedores en la base de datos
package com.licoreria.licoreria0.modelo;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "proveedores")
public class Proveedor {
    // datos basicos del proveedor definidos en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor_pk")
    private Long idProveedor;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String ruc;

    private String telefono;
    private String direccion;
    private String correo;

    // Un proveedor puede tener MUCHOS productos
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();

    // constructores 

    public Proveedor() {
    }

    public Proveedor(String nombre, String ruc, String telefono, String direccion, String correo) {
        this.nombre = nombre;
        this.ruc = ruc;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }
    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    // sirve para evitar problemas de referencia circular al convertir a JSON
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}