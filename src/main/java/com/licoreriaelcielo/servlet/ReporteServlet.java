package com.licoreriaelcielo.servlet;

import java.io.IOException;

import com.licoreriaelcielo.dao.PedidoDao;
import com.licoreriaelcielo.dao.ProductoDao;
import com.licoreriaelcielo.dao.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de REPORTES (admin): resumen general y listado de pedidos.
 */
@WebServlet("/reportes")
public class ReporteServlet extends HttpServlet {

    private final ProductoDao productoDao = new ProductoDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final PedidoDao pedidoDao = new PedidoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }
        request.setAttribute("totalProductos", productoDao.listarProductos().size());
        request.setAttribute("totalUsuarios", usuarioDao.listarUsuarios().size());
        request.setAttribute("totalPedidos", pedidoDao.contarPedidos());
        request.setAttribute("totalVentas", pedidoDao.sumaVentas());
        request.setAttribute("pedidos", pedidoDao.listarTodos());
        request.setAttribute("vistaActual", "reportes");
        request.getRequestDispatcher("/WEB-INF/jsp/reportes.jsp").forward(request, response);
    }
}
