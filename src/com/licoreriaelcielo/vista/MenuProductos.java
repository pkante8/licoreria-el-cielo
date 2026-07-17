package com.licoreriaelcielo.vista;

import java.util.List;
import java.util.Scanner;

import com.licoreriaelcielo.dao.ProductoDao;
import com.licoreriaelcielo.modelo.Producto;

/**
 * Clase principal del módulo de productos de la Licorería El Cielo.
 * Muestra un menú por consola que permite ejecutar las operaciones
 * CRUD sobre el inventario, usando la conexión JDBC a MySQL.
 */
public class MenuProductos {

    // Constantes del módulo
    private static final int OPCION_SALIR = 6;

    // Objetos compartidos por los métodos del menú
    private static final Scanner lector = new Scanner(System.in);
    private static final ProductoDao productoDao = new ProductoDao();

    /**
     * Método principal: muestra el menú y ejecuta la opción elegida.
     */
    public static void main(String[] args) {
        int opcionElegida = 0;

        System.out.println("==============================================");
        System.out.println("   LICORERÍA EL CIELO - MÓDULO DE PRODUCTOS");
        System.out.println("==============================================");

        // Repetir el menú hasta que el usuario elija salir
        while (opcionElegida != OPCION_SALIR) {
            mostrarMenu();
            opcionElegida = leerNumeroEntero("Elija una opción: ");

            switch (opcionElegida) {
                case 1 -> listarProductos();
                case 2 -> buscarProducto();
                case 3 -> agregarProducto();
                case 4 -> actualizarProducto();
                case 5 -> eliminarProducto();
                case OPCION_SALIR -> System.out.println("Gracias por usar el sistema. ¡Hasta pronto!");
                default -> System.out.println("Opción no válida, intente de nuevo.");
            }
        }
        lector.close();
    }

    /**
     * Muestra las opciones del menú principal.
     */
    private static void mostrarMenu() {
        System.out.println("\n----------- MENÚ PRINCIPAL -----------");
        System.out.println("1. Listar productos");
        System.out.println("2. Buscar producto por ID");
        System.out.println("3. Agregar producto");
        System.out.println("4. Actualizar producto");
        System.out.println("5. Eliminar producto");
        System.out.println("6. Salir");
    }

    /**
     * Muestra en pantalla todos los productos del inventario.
     */
    private static void listarProductos() {
        List<Producto> listaProductos = productoDao.listarProductos();

        if (listaProductos.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }

        System.out.println("\n------------------- INVENTARIO -------------------");
        System.out.printf("%-4s %-20s %-12s %12s %7s%n", "ID", "NOMBRE", "CATEGORÍA", "PRECIO", "STOCK");
        System.out.println("---------------------------------------------------");
        for (Producto producto : listaProductos) {
            System.out.printf("%-4d %-20s %-12s %12s %7d%n",
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getCategoria(),
                    formatearPrecio(producto.getPrecio()),
                    producto.getStock());
        }
    }

    /**
     * Busca un producto por su ID y muestra todos sus datos.
     */
    private static void buscarProducto() {
        int idProducto = leerNumeroEntero("Ingrese el ID del producto: ");
        Producto producto = productoDao.buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("No se encontró un producto con el ID " + idProducto + ".");
            return;
        }

        System.out.println("\n--------- DETALLE DEL PRODUCTO ---------");
        System.out.println("ID:            " + producto.getIdProducto());
        System.out.println("Nombre:        " + producto.getNombre());
        System.out.println("Descripción:   " + producto.getDescripcion());
        System.out.println("Categoría:     " + producto.getCategoria());
        System.out.println("Precio:        " + formatearPrecio(producto.getPrecio()));
        System.out.println("Stock:         " + producto.getStock());
        System.out.println("Volumen:       " + producto.getVolumen());
        System.out.println("Grado alcohol: " + producto.getGradoAlcohol());
        System.out.println("Origen:        " + producto.getOrigen());
    }

    /**
     * Solicita los datos de un nuevo producto y lo registra en la base de datos.
     */
    private static void agregarProducto() {
        System.out.println("\n--------- AGREGAR PRODUCTO ---------");
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(leerTexto("Nombre: "));
        nuevoProducto.setDescripcion(leerTexto("Descripción: "));
        nuevoProducto.setCategoria(leerTexto("Categoría: "));
        nuevoProducto.setPrecio(leerNumeroDecimal("Precio: "));
        nuevoProducto.setStock(leerNumeroEntero("Stock: "));
        nuevoProducto.setVolumen(leerTexto("Volumen (ej: 750 ml): "));
        nuevoProducto.setGradoAlcohol(leerTexto("Grado de alcohol (ej: 40%): "));
        nuevoProducto.setOrigen(leerTexto("Origen: "));

        if (productoDao.agregarProducto(nuevoProducto)) {
            System.out.println("Producto agregado correctamente.");
        } else {
            System.out.println("No fue posible agregar el producto.");
        }
    }

    /**
     * Busca un producto por su ID y permite actualizar sus datos.
     */
    private static void actualizarProducto() {
        int idProducto = leerNumeroEntero("Ingrese el ID del producto a actualizar: ");
        Producto producto = productoDao.buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("No se encontró un producto con el ID " + idProducto + ".");
            return;
        }

        System.out.println("Producto actual: " + producto.getNombre()
                + " | Precio: " + formatearPrecio(producto.getPrecio())
                + " | Stock: " + producto.getStock());
        System.out.println("Ingrese los nuevos datos:");

        producto.setNombre(leerTexto("Nombre: "));
        producto.setDescripcion(leerTexto("Descripción: "));
        producto.setCategoria(leerTexto("Categoría: "));
        producto.setPrecio(leerNumeroDecimal("Precio: "));
        producto.setStock(leerNumeroEntero("Stock: "));
        producto.setVolumen(leerTexto("Volumen: "));
        producto.setGradoAlcohol(leerTexto("Grado de alcohol: "));
        producto.setOrigen(leerTexto("Origen: "));

        if (productoDao.actualizarProducto(producto)) {
            System.out.println("Producto actualizado correctamente.");
        } else {
            System.out.println("No fue posible actualizar el producto.");
        }
    }

    /**
     * Elimina un producto del inventario, previa confirmación del usuario.
     */
    private static void eliminarProducto() {
        int idProducto = leerNumeroEntero("Ingrese el ID del producto a eliminar: ");
        Producto producto = productoDao.buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("No se encontró un producto con el ID " + idProducto + ".");
            return;
        }

        String confirmacion = leerTexto("¿Está seguro de eliminar \"" + producto.getNombre() + "\"? (s/n): ");
        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        if (productoDao.eliminarProducto(idProducto)) {
            System.out.println("Producto eliminado correctamente.");
        } else {
            System.out.println("No fue posible eliminar el producto.");
        }
    }

    /**
     * Lee un texto ingresado por el usuario.
     */
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return lector.nextLine().trim();
    }

    /**
     * Lee un número entero validando que la entrada sea correcta.
     */
    private static int leerNumeroEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(lector.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee un número decimal validando que la entrada sea correcta.
     */
    private static double leerNumeroDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(lector.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    /**
     * Formatea un valor numérico como precio en pesos colombianos.
     * Ejemplo: 360000 se muestra como $ 360.000
     */
    private static String formatearPrecio(double valor) {
        return String.format("$ %,.0f", valor).replace(",", ".");
    }
}
