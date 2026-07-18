package com.licoreriaelcielo.servlet;

import java.io.IOException;

import com.licoreriaelcielo.dao.ProductoDao;
import com.licoreriaelcielo.modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador del DETALLE DE PRODUCTO.
 * Recibe el id por parámetro y muestra la ficha del producto.
 */
@WebServlet("/producto")
public class DetalleProductoServlet extends HttpServlet {

    private final ProductoDao productoDao = new ProductoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = parseId(request.getParameter("id"));
        Producto producto = (id > 0) ? productoDao.buscarProductoPorId(id) : null;

        if (producto == null) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }
        request.setAttribute("producto", producto);
        request.setAttribute("vistaActual", "catalogo");
        request.getRequestDispatcher("/WEB-INF/jsp/detalle.jsp").forward(request, response);
    }

    private int parseId(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
