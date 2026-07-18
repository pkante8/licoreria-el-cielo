package com.licoreriaelcielo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.licoreriaelcielo.conexion.ConexionBaseDatos;
import com.licoreriaelcielo.modelo.Pedido;
import com.licoreriaelcielo.modelo.PedidoItem;

/**
 * DAO del módulo de pedidos. Persiste la cabecera y el detalle de cada
 * pedido dentro de una transacción, y consulta los pedidos por usuario.
 */
public class PedidoDao {

    /**
     * Crea un pedido con sus ítems en una transacción.
     *
     * @param pedido pedido a registrar (con items y total ya calculados).
     * @return id del pedido creado, o -1 si falló.
     */
    public int crearPedido(Pedido pedido) {
        String sqlCab = "INSERT INTO pedidos (id_usuario, estado, total) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO pedido_items (id_pedido, id_producto, nombre_producto, "
                + "cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?)";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return -1;
        }
        try {
            conexion.setAutoCommit(false);

            int idPedido;
            try (PreparedStatement ps = conexion.prepareStatement(sqlCab, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pedido.getIdUsuario());
                ps.setString(2, pedido.getEstado() != null ? pedido.getEstado() : "En preparación");
                ps.setDouble(3, pedido.getTotal());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conexion.rollback();
                        return -1;
                    }
                    idPedido = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = conexion.prepareStatement(sqlItem)) {
                for (PedidoItem it : pedido.getItems()) {
                    ps.setInt(1, idPedido);
                    ps.setInt(2, it.getIdProducto());
                    ps.setString(3, it.getNombreProducto());
                    ps.setInt(4, it.getCantidad());
                    ps.setDouble(5, it.getPrecioUnitario());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conexion.commit();
            return idPedido;
        } catch (SQLException e) {
            System.out.println("Error al crear el pedido: " + e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    /**
     * Lista los pedidos de un usuario (con sus ítems), del más reciente al más antiguo.
     */
    public List<Pedido> listarPorUsuario(int idUsuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT id_pedido, DATE_FORMAT(fecha, '%Y-%m-%d %H:%i') AS fecha_fmt, estado, total "
                + "FROM pedidos WHERE id_usuario = ? ORDER BY id_pedido DESC";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return pedidos;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdPedido(rs.getInt("id_pedido"));
                    p.setIdUsuario(idUsuario);
                    p.setFecha(rs.getString("fecha_fmt"));
                    p.setEstado(rs.getString("estado"));
                    p.setTotal(rs.getDouble("total"));
                    p.setItems(listarItems(conexion, p.getIdPedido()));
                    pedidos.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los pedidos: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return pedidos;
    }

    /** Número total de pedidos registrados (para el dashboard/reportes). */
    public int contarPedidos() {
        return unEntero("SELECT COUNT(*) FROM pedidos");
    }

    /** Suma de ventas de todos los pedidos (para el dashboard/reportes). */
    public double sumaVentas() {
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return 0;
        }
        try (PreparedStatement ps = conexion.prepareStatement("SELECT COALESCE(SUM(total),0) FROM pedidos");
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error al sumar ventas: " + e.getMessage());
            return 0;
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    /** Lista todos los pedidos (con el nombre del cliente) para reportes. */
    public List<Pedido> listarTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id_pedido, DATE_FORMAT(p.fecha,'%Y-%m-%d %H:%i') AS fecha_fmt, "
                + "p.estado, p.total, CONCAT(u.nombres,' ',COALESCE(u.apellidos,'')) AS cliente "
                + "FROM pedidos p JOIN usuarios u ON u.id_usuario = p.id_usuario "
                + "ORDER BY p.id_pedido DESC";
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return pedidos;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setIdPedido(rs.getInt("id_pedido"));
                p.setFecha(rs.getString("fecha_fmt"));
                p.setEstado(rs.getString("estado") + " · " + rs.getString("cliente"));
                p.setTotal(rs.getDouble("total"));
                pedidos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar todos los pedidos: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return pedidos;
    }

    private int unEntero(String sql) {
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return 0;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.out.println("Error en consulta de conteo: " + e.getMessage());
            return 0;
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    private List<PedidoItem> listarItems(Connection conexion, int idPedido) throws SQLException {
        List<PedidoItem> items = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_producto, cantidad, precio_unitario "
                + "FROM pedido_items WHERE id_pedido = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new PedidoItem(
                            rs.getInt("id_producto"),
                            rs.getString("nombre_producto"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario")));
                }
            }
        }
        return items;
    }
}
