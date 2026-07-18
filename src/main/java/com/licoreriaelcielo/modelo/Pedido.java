package com.licoreriaelcielo.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Cabecera de un pedido (tabla pedidos) con su lista de ítems.
 */
public class Pedido {

    private int idPedido;
    private int idUsuario;
    private String fecha;
    private String estado;
    private double total;
    private List<PedidoItem> items = new ArrayList<>();

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<PedidoItem> getItems() {
        return items;
    }

    public void setItems(List<PedidoItem> items) {
        this.items = items;
    }

    public int getCantidadItems() {
        return items.size();
    }
}
