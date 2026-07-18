package com.licoreriaelcielo.modelo;

/**
 * Representa una línea del carrito de compras: un producto y su cantidad.
 * Se guarda en la sesión del usuario (no en la base de datos).
 */
public class ItemCarrito {

    private Producto producto;
    private int cantidad;

    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /** Subtotal de la línea (precio * cantidad). */
    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}
