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
 * Controlador del módulo CATÁLOGO (cliente).
 * Lista los productos desde la base de datos (reutiliza ProductoDao).
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private final ProductoDao productoDao = new ProductoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Producto> productos = productoDao.listarProductos();
        request.setAttribute("productos", productos);
        request.setAttribute("vistaActual", "catalogo");
        request.getRequestDispatcher("/WEB-INF/jsp/catalogo.jsp").forward(request, response);
    }
}
