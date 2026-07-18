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
 * Clase DAO (Data Access Object) del módulo de productos.
 * Contiene las operaciones CRUD (crear, leer, actualizar y eliminar)
 * sobre la tabla productos, usando JDBC con sentencias preparadas.
 */
public class ProductoDao {

    /**
     * Consulta y retorna todos los productos registrados en la base de datos.
     *
     * @return lista de productos.
     */
    public List<Producto> listarProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        // Validar que la conexión se haya establecido correctamente
        if (conexion == null) {
            return listaProductos;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            // Recorrer los resultados y convertir cada fila en un objeto Producto
            while (resultado.next()) {
                Producto producto = convertirFilaEnProducto(resultado);
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los productos: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return listaProductos;
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param idProducto identificador del producto a buscar.
     * @return el producto encontrado o null si no existe.
     */
    public Producto buscarProductoPorId(int idProducto) {
        Producto producto = null;
        String sql = "SELECT * FROM productos WHERE id_producto = ?";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        // Validar que la conexión se haya establecido correctamente
        if (conexion == null) {
            return null;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idProducto);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    producto = convertirFilaEnProducto(resultado);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el producto: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return producto;
    }

    /**
     * Registra un nuevo producto en la base de datos.
     *
     * @param producto el producto que se desea agregar.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean agregarProducto(Producto producto) {
        boolean registroExitoso = false;
        String sql = "INSERT INTO productos (nombre, descripcion, categoria, precio, stock, "
                + "volumen, grado_alcohol, origen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        // Validar que la conexión se haya establecido correctamente
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, producto.getNombre());
            sentencia.setString(2, producto.getDescripcion());
            sentencia.setString(3, producto.getCategoria());
            sentencia.setDouble(4, producto.getPrecio());
            sentencia.setInt(5, producto.getStock());
            sentencia.setString(6, producto.getVolumen());
            sentencia.setString(7, producto.getGradoAlcohol());
            sentencia.setString(8, producto.getOrigen());

            int filasAfectadas = sentencia.executeUpdate();
            registroExitoso = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al agregar el producto: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return registroExitoso;
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param producto el producto con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarProducto(Producto producto) {
        boolean actualizacionExitosa = false;
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, categoria = ?, "
                + "precio = ?, stock = ?, volumen = ?, grado_alcohol = ?, origen = ? "
                + "WHERE id_producto = ?";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        // Validar que la conexión se haya establecido correctamente
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, producto.getNombre());
            sentencia.setString(2, producto.getDescripcion());
            sentencia.setString(3, producto.getCategoria());
            sentencia.setDouble(4, producto.getPrecio());
            sentencia.setInt(5, producto.getStock());
            sentencia.setString(6, producto.getVolumen());
            sentencia.setString(7, producto.getGradoAlcohol());
            sentencia.setString(8, producto.getOrigen());
            sentencia.setInt(9, producto.getIdProducto());

            int filasAfectadas = sentencia.executeUpdate();
            actualizacionExitosa = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return actualizacionExitosa;
    }

    /**
     * Elimina un producto de la base de datos por su identificador.
     *
     * @param idProducto identificador del producto a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarProducto(int idProducto) {
        boolean eliminacionExitosa = false;
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        // Validar que la conexión se haya establecido correctamente
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idProducto);
            int filasAfectadas = sentencia.executeUpdate();
            eliminacionExitosa = filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return eliminacionExitosa;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Producto.
     *
     * @param resultado fila actual del ResultSet.
     * @return objeto Producto con los datos de la fila.
     * @throws SQLException si ocurre un error al leer los datos.
     */
    private Producto convertirFilaEnProducto(ResultSet resultado) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("id_producto"));
        producto.setNombre(resultado.getString("nombre"));
        producto.setDescripcion(resultado.getString("descripcion"));
        producto.setCategoria(resultado.getString("categoria"));
        producto.setPrecio(resultado.getDouble("precio"));
        producto.setStock(resultado.getInt("stock"));
        producto.setVolumen(resultado.getString("volumen"));
        producto.setGradoAlcohol(resultado.getString("grado_alcohol"));
        producto.setOrigen(resultado.getString("origen"));
        return producto;
    }
}
