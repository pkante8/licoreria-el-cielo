package com.licoreriaelcielo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.licoreriaelcielo.conexion.ConexionBaseDatos;
import com.licoreriaelcielo.modelo.Producto;

/**
 * DAO del módulo de favoritos (relación usuario - producto).
 */
public class FavoritoDao {

    /** Indica si un producto ya es favorito del usuario. */
    public boolean esFavorito(int idUsuario, int idProducto) {
        String sql = "SELECT 1 FROM favoritos WHERE id_usuario = ? AND id_producto = ?";
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar favorito: " + e.getMessage());
            return false;
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    /** Alterna el favorito: si existe lo quita, si no lo agrega. */
    public void alternar(int idUsuario, int idProducto) {
        if (esFavorito(idUsuario, idProducto)) {
            ejecutar("DELETE FROM favoritos WHERE id_usuario = ? AND id_producto = ?", idUsuario, idProducto);
        } else {
            ejecutar("INSERT INTO favoritos (id_usuario, id_producto) VALUES (?, ?)", idUsuario, idProducto);
        }
    }

    private void ejecutar(String sql, int idUsuario, int idProducto) {
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al modificar favorito: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    /** Lista los productos marcados como favoritos por el usuario. */
    public List<Producto> listarProductosFavoritos(int idUsuario) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.* FROM productos p "
                + "JOIN favoritos f ON f.id_producto = p.id_producto "
                + "WHERE f.id_usuario = ?";
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return lista;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setVolumen(rs.getString("volumen"));
                    p.setGradoAlcohol(rs.getString("grado_alcohol"));
                    p.setOrigen(rs.getString("origen"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar favoritos: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return lista;
    }
}
