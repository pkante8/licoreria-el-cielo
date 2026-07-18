package com.licoreriaelcielo.servlet;

import java.io.IOException;

import com.licoreriaelcielo.dao.UsuarioDao;
import com.licoreriaelcielo.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador del módulo de LOGIN.
 * Reemplaza la lógica de autenticación que antes estaba en script.js:
 * valida campos, formato de correo, credenciales y coherencia de rol,
 * y crea la sesión del usuario.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    /** Muestra el formulario de login. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si ya hay sesión, redirigir a la pantalla principal del rol.
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    /** Procesa el envío del formulario de login. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = trim(request.getParameter("email"));
        String password = trim(request.getParameter("password"));
        String rolSeleccionado = request.getParameter("rol"); // pestaña cliente/admin

        // 1. Campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            mostrarError(request, response, "Para continuar, completa todos los campos obligatorios.", email);
            return;
        }

        // 2. Formato de correo
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            mostrarError(request, response, "El correo electrónico no tiene un formato válido.", email);
            return;
        }

        // 3. Credenciales
        Usuario usuario = usuarioDao.autenticar(email, password);
        if (usuario == null) {
            mostrarError(request, response, "Usuario o contraseña incorrectos.", email);
            return;
        }

        // 4. Usuario suspendido
        if ("suspendido".equalsIgnoreCase(usuario.getEstado())) {
            mostrarError(request, response, "Tu cuenta está suspendida. Contacta al administrador.", email);
            return;
        }

        // 5. Coherencia de rol con la pestaña seleccionada
        if (rolSeleccionado != null && !rolSeleccionado.equals(usuario.getRol())) {
            mostrarError(request, response, "No tienes los permisos para ingresar por ese rol.", email);
            return;
        }

        // Login exitoso: crear sesión
        HttpSession sesion = request.getSession(true);
        sesion.setAttribute("usuario", usuario);
        sesion.setAttribute("rol", usuario.getRol());

        response.sendRedirect(request.getContextPath() + "/inicio");
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
            String mensaje, String email) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    private String trim(String valor) {
        return (valor == null) ? "" : valor.trim();
    }
}
