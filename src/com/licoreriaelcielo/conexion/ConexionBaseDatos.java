package com.licoreriaelcielo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de establecer la conexión con la base de datos MySQL
 * de la Licorería El Cielo por medio de JDBC.
 */
public class ConexionBaseDatos {

    // Datos de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/licoreria_el_cielo";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Establece y retorna la conexión con la base de datos.
     *
     * @return objeto Connection si la conexión fue exitosa, null en caso de error.
     */
    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establecer la conexión con la base de datos
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: no se encontró el driver JDBC de MySQL. " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    /**
     * Cierra la conexión con la base de datos.
     *
     * @param conexion la conexión que se desea cerrar.
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
