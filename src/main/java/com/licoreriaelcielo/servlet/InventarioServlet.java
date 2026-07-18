package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.List;

import com.licoreriaelcielo.dao.ProductoDao;
import com.licoreriaelcielo.modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador del módulo INVENTARIO (admin): CRUD de productos.
 * Reutiliza ProductoDao. Solo accesible para administradores.
 */
@WebServlet("/inventario")
public class InventarioServlet extends HttpServlet {

    private final ProductoDao productoDao = new ProductoDao();

    /** Lista los productos y calcula las estadísticas del inventario. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!esAdmin(request, response)) {
            return;
        }
        List<Producto> productos = productoDao.listarProductos();

        int total = productos.size();
        int stockBajo = 0;
        int agotados = 0;
        double valorTotal = 0;
        for (Producto p : productos) {
            if (p.getStock() == 0) {
                agotados++;
            } else if (p.getStock() < 10) {
                stockBajo++;
            }
            valorTotal += p.getPrecio() * p.getStock();
        }

        // Modo edición: si viene ?editar=ID, precargar ese producto en el formulario.
        String editarId = request.getParameter("editar");
        if (editarId != null) {
            Producto editando = productoDao.buscarProductoPorId(parseInt(editarId, 0));
            if (editando != null) {
                request.setAttribute("editando", editando);
            }
        }

        request.setAttribute("productos", productos);
        request.setAttribute("total", total);
        request.setAttribute("stockBajo", stockBajo);
        request.setAttribute("agotados", agotados);
        request.setAttribute("valorTotal", valorTotal);
        request.setAttribute("vistaActual", "inventario");
        request.getRequestDispatcher("/WEB-INF/jsp/inventario.jsp").forward(request, response);
    }

    /** Procesa las acciones: agregar, actualizar y eliminar. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!esAdmin(request, response)) {
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        switch (accion) {
            case "agregar":
                productoDao.agregarProducto(leerProducto(request, false));
                break;
            case "actualizar":
                productoDao.actualizarProducto(leerProducto(request, true));
                break;
            case "eliminar":
                productoDao.eliminarProducto(parseInt(request.getParameter("id"), 0));
                break;
            default:
                break;
        }
        // Patrón POST-Redirect-GET para evitar reenvíos del formulario.
        response.sendRedirect(request.getContextPath() + "/inventario");
    }

    /** Construye un Producto a partir de los parámetros del formulario. */
    private Producto leerProducto(HttpServletRequest request, boolean conId) {
        Producto p = new Producto();
        if (conId) {
            p.setIdProducto(parseInt(request.getParameter("id"), 0));
        }
        p.setNombre(trim(request.getParameter("nombre")));
        p.setDescripcion(trim(request.getParameter("descripcion")));
        p.setCategoria(trim(request.getParameter("categoria")));
        p.setPrecio(parseDouble(request.getParameter("precio")));
        p.setStock(parseInt(request.getParameter("stock"), 0));
        p.setVolumen(trim(request.getParameter("volumen")));
        p.setGradoAlcohol(trim(request.getParameter("gradoAlcohol")));
        p.setOrigen(trim(request.getParameter("origen")));
        return p;
    }

    private boolean esAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object rol = request.getSession().getAttribute("rol");
        if (!"admin".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return false;
        }
        return true;
    }

    private String trim(String v) {
        return (v == null) ? "" : v.trim();
    }

    private int parseInt(String v, int porDefecto) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return porDefecto;
        }
    }

    private double parseDouble(String v) {
        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }
}
