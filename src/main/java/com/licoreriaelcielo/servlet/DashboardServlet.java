package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.List;

import com.licoreriaelcielo.dao.PedidoDao;
import com.licoreriaelcielo.dao.ProductoDao;
import com.licoreriaelcielo.dao.UsuarioDao;
import com.licoreriaelcielo.modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador del DASHBOARD (admin): KPIs generales calculados desde la BD.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

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

        List<Producto> productos = productoDao.listarProductos();
        int stockBajo = 0;
        for (Producto p : productos) {
            if (p.getStock() < 10) {
                stockBajo++;
            }
        }

        request.setAttribute("totalProductos", productos.size());
        request.setAttribute("stockBajo", stockBajo);
        request.setAttribute("totalUsuarios", usuarioDao.listarUsuarios().size());
        request.setAttribute("totalPedidos", pedidoDao.contarPedidos());
        request.setAttribute("totalVentas", pedidoDao.sumaVentas());
        request.setAttribute("vistaActual", "dashboard");
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
    }
}
