package com.licoreriaelcielo.modelo;

/**
 * Clase que representa un producto del inventario de la Licorería El Cielo.
 * Aplica el principio de encapsulamiento: atributos privados con
 * métodos públicos get y set.
 */
public class Producto {

    // Atributos privados (encapsulamiento)
    private int idProducto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private double precio;
    private int stock;
    private String volumen;
    private String gradoAlcohol;
    private String origen;

    /**
     * Constructor vacío.
     */
    public Producto() {
    }

    /**
     * Constructor con todos los atributos del producto.
     */
    public Producto(int idProducto, String nombre, String descripcion, String categoria,
            double precio, int stock, String volumen, String gradoAlcohol, String origen) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.volumen = volumen;
        this.gradoAlcohol = gradoAlcohol;
        this.origen = origen;
    }

    // Métodos get y set
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getGradoAlcohol() {
        return gradoAlcohol;
    }

    public void setGradoAlcohol(String gradoAlcohol) {
        this.gradoAlcohol = gradoAlcohol;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
