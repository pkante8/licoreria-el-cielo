package com.licoreriaelcielo.servlet;

import java.io.IOException;

import com.licoreriaelcielo.dao.UsuarioDao;
import com.licoreriaelcielo.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de GESTIÓN DE USUARIOS (admin): lista y suspende/reactiva usuarios.
 */
@WebServlet("/usuarios")
public class UsuarioAdminServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!esAdmin(request, response)) {
            return;
        }
        request.setAttribute("usuarios", usuarioDao.listarUsuarios());
        request.setAttribute("vistaActual", "usuarios");
        request.getRequestDispatcher("/WEB-INF/jsp/usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!esAdmin(request, response)) {
            return;
        }
        if ("toggle".equals(request.getParameter("accion"))) {
            int id = parseInt(request.getParameter("id"));
            Usuario u = usuarioDao.buscarPorId(id);
            if (u != null) {
                String nuevo = "activo".equals(u.getEstado()) ? "suspendido" : "activo";
                usuarioDao.cambiarEstado(id, nuevo);
            }
        }
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }

    private boolean esAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"admin".equals(request.getSession().getAttribute("rol"))) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return false;
        }
        return true;
    }

    private int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
