package com.licoreria.licoreria0.modelo;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente_pk")
    private Long idCliente;

    @Column(length = 150)
    private String nombre;

    @Column(length = 20)
    private String cedula;

    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Column(length = 100)
    private String correo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Venta> ventas = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String nombre, String cedula, String telefono, String direccion, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
}
