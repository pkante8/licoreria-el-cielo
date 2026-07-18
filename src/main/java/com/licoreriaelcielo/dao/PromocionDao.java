package com.licoreriaelcielo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.licoreriaelcielo.conexion.ConexionBaseDatos;
import com.licoreriaelcielo.modelo.Promocion;

/**
 * DAO del módulo de promociones.
 */
public class PromocionDao {

    public List<Promocion> listarActivas() {
        return listar("SELECT * FROM promociones WHERE activa = TRUE ORDER BY id_promocion");
    }

    public List<Promocion> listarTodas() {
        return listar("SELECT * FROM promociones ORDER BY id_promocion");
    }

    private List<Promocion> listar(String sql) {
        List<Promocion> lista = new ArrayList<>();
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return lista;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(convertir(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar promociones: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return lista;
    }

    public boolean crear(Promocion p) {
        String sql = "INSERT INTO promociones (nombre, producto, tipo, valor, fecha_inicio, fecha_fin, activa) "
                + "VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getProducto());
            ps.setString(3, p.getTipo());
            ps.setInt(4, p.getValor());
            ps.setString(5, vacioANull(p.getFechaInicio()));
            ps.setString(6, vacioANull(p.getFechaFin()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al crear promoción: " + e.getMessage());
            return false;
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    public boolean eliminar(int idPromocion) {
        String sql = "DELETE FROM promociones WHERE id_promocion = ?";
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPromocion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar promoción: " + e.getMessage());
            return false;
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    private String vacioANull(String v) {
        return (v == null || v.isEmpty()) ? null : v;
    }

    private Promocion convertir(ResultSet rs) throws SQLException {
        Promocion p = new Promocion();
        p.setIdPromocion(rs.getInt("id_promocion"));
        p.setNombre(rs.getString("nombre"));
        p.setProducto(rs.getString("producto"));
        p.setTipo(rs.getString("tipo"));
        p.setValor(rs.getInt("valor"));
        p.setFechaInicio(rs.getString("fecha_inicio"));
        p.setFechaFin(rs.getString("fecha_fin"));
        p.setActiva(rs.getBoolean("activa"));
        return p;
    }
}
