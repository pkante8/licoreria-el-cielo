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
 * Controlador del módulo de REGISTRO de usuarios.
 * Los registros desde la pantalla pública siempre crean clientes.
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    /** Muestra el formulario de registro. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(request, response);
    }

    /** Procesa el envío del formulario de registro. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombres = trim(request.getParameter("nombres"));
        String apellidos = trim(request.getParameter("apellidos"));
        String cedula = trim(request.getParameter("cedula"));
        String email = trim(request.getParameter("email"));
        String password = trim(request.getParameter("password"));

        // 1. Campos obligatorios
        if (nombres.isEmpty() || apellidos.isEmpty() || cedula.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            mostrarError(request, response, "Debes completar todos los campos.");
            return;
        }

        // 2. Formato de correo
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            mostrarError(request, response, "El correo electrónico no tiene un formato válido.");
            return;
        }

        // 3. Correo ya registrado
        if (usuarioDao.existeEmail(email)) {
            mostrarError(request, response, "Ya existe una cuenta con ese correo.");
            return;
        }

        // 4. Registrar como cliente
        Usuario nuevo = new Usuario();
        nuevo.setNombres(nombres);
        nuevo.setApellidos(apellidos);
        nuevo.setCedula(cedula);
        nuevo.setEmail(email);
        nuevo.setPassword(password);
        nuevo.setRol("cliente");
        nuevo.setEstado("activo");

        if (usuarioDao.registrar(nuevo)) {
            request.setAttribute("exito", "Usuario registrado. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            mostrarError(request, response, "No se pudo registrar el usuario. Intenta de nuevo.");
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
            String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(request, response);
    }

    private String trim(String valor) {
        return (valor == null) ? "" : valor.trim();
    }
}
