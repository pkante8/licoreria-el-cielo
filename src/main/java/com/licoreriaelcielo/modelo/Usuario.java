package com.licoreriaelcielo.modelo;

/**
 * Clase modelo que representa un usuario del sistema (cliente o administrador).
 * Aplica encapsulamiento con atributos privados y métodos get/set.
 */
public class Usuario {

    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private String cedula;
    private String telefono;
    private String direccion;
    private String rol;     // "cliente" o "admin"
    private String estado;  // "activo" o "suspendido"

    public Usuario() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Nombre completo para mostrar en la interfaz. */
    public String getNombreCompleto() {
        String ap = (apellidos != null) ? apellidos : "";
        return (nombres + " " + ap).trim();
    }
}
