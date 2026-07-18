package com.licoreriaelcielo.servlet;

import java.io.IOException;

import com.licoreriaelcielo.dao.PromocionDao;
import com.licoreriaelcielo.modelo.Promocion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de GESTIÓN DE PROMOCIONES (admin): crear y eliminar promociones.
 */
@WebServlet("/admin-promociones")
public class PromocionAdminServlet extends HttpServlet {

    private final PromocionDao promocionDao = new PromocionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!esAdmin(request, response)) {
            return;
        }
        request.setAttribute("promociones", promocionDao.listarTodas());
        request.setAttribute("vistaActual", "admin-promociones");
        request.getRequestDispatcher("/WEB-INF/jsp/admin-promociones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!esAdmin(request, response)) {
            return;
        }
        String accion = request.getParameter("accion");
        if ("crear".equals(accion)) {
            Promocion p = new Promocion();
            p.setNombre(trim(request.getParameter("nombre")));
            p.setProducto(trim(request.getParameter("producto")));
            p.setTipo(trim(request.getParameter("tipo")));
            p.setValor(parseInt(request.getParameter("valor")));
            p.setFechaInicio(trim(request.getParameter("fechaInicio")));
            p.setFechaFin(trim(request.getParameter("fechaFin")));
            if (!p.getNombre().isEmpty()) {
                promocionDao.crear(p);
            }
        } else if ("eliminar".equals(accion)) {
            promocionDao.eliminar(parseInt(request.getParameter("id")));
        }
        response.sendRedirect(request.getContextPath() + "/admin-promociones");
    }

    private boolean esAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return false;
        }
        return true;
    }

    private String trim(String v) {
        return (v == null) ? "" : v.trim();
    }

    private int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
