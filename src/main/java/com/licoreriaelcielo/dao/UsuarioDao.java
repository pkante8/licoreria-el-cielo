package com.licoreriaelcielo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.licoreriaelcielo.conexion.ConexionBaseDatos;
import com.licoreriaelcielo.modelo.Usuario;

/**
 * DAO del módulo de usuarios. Contiene las operaciones de autenticación
 * (login), registro y gestión de usuarios, usando JDBC con sentencias
 * preparadas para evitar inyección SQL.
 */
public class UsuarioDao {

    /**
     * Valida las credenciales de un usuario (login).
     *
     * @param email    correo del usuario.
     * @param password contraseña ingresada.
     * @return el usuario si las credenciales son correctas, null en caso contrario.
     */
    public Usuario autenticar(String email, String password) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return null;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, email);
            sentencia.setString(2, password);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    usuario = convertirFilaEnUsuario(resultado);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al autenticar el usuario: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return usuario;
    }

    /**
     * Verifica si ya existe un usuario con el correo indicado.
     *
     * @param email correo a verificar.
     * @return true si el correo ya está registrado.
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT 1 FROM usuarios WHERE email = ?";
        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, email);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el correo: " + e.getMessage());
            return false;
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
    }

    /**
     * Registra un nuevo usuario (siempre con rol 'cliente' desde el registro público).
     *
     * @param usuario datos del usuario a registrar.
     * @return true si el registro fue exitoso.
     */
    public boolean registrar(Usuario usuario) {
        boolean exito = false;
        String sql = "INSERT INTO usuarios (nombres, apellidos, email, password, cedula, "
                + "telefono, direccion, rol, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return false;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombres());
            sentencia.setString(2, usuario.getApellidos());
            sentencia.setString(3, usuario.getEmail());
            sentencia.setString(4, usuario.getPassword());
            sentencia.setString(5, usuario.getCedula());
            sentencia.setString(6, usuario.getTelefono());
            sentencia.setString(7, usuario.getDireccion());
            sentencia.setString(8, usuario.getRol() != null ? usuario.getRol() : "cliente");
            sentencia.setString(9, usuario.getEstado() != null ? usuario.getEstado() : "activo");

            int filas = sentencia.executeUpdate();
            exito = filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return exito;
    }

    /**
     * Lista todos los usuarios (para el módulo de administración).
     *
     * @return lista de usuarios.
     */
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario";

        Connection conexion = ConexionBaseDatos.obtenerConexion();
        if (conexion == null) {
            return lista;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                lista.add(convertirFilaEnUsuario(resultado));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los usuarios: " + e.getMessage());
        } finally {
            ConexionBaseDatos.cerrarConexion(conexion);
        }
        return lista;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Usuario.
     */
    private Usuario convertirFilaEnUsuario(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("id_usuario"));
        usuario.setNombres(resultado.getString("nombres"));
        usuario.setApellidos(resultado.getString("apellidos"));
        usuario.setEmail(resultado.getString("email"));
        usuario.setPassword(resultado.getString("password"));
        usuario.setCedula(resultado.getString("cedula"));
        usuario.setTelefono(resultado.getString("telefono"));
        usuario.setDireccion(resultado.getString("direccion"));
        usuario.setRol(resultado.getString("rol"));
        usuario.setEstado(resultado.getString("estado"));
        return usuario;
    }
}
