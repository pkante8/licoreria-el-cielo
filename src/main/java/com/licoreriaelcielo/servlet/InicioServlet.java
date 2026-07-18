package com.licoreriaelcielo.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Pantalla de inicio tras el login (placeholder de la Fase 1).
 * En fases siguientes, según el rol, redirigirá al catálogo (cliente)
 * o al dashboard (admin). Por ahora muestra una bienvenida que
 * confirma que la autenticación y la sesión funcionan.
 */
@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir a la pantalla principal según el rol.
        Object rol = request.getSession().getAttribute("rol");
        if ("admin".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/inventario");
        } else {
            response.sendRedirect(request.getContextPath() + "/catalogo");
        }
    }
}
