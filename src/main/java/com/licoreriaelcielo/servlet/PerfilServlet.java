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
 * Controlador de MI PERFIL: muestra y actualiza los datos del usuario.
 */
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("vistaActual", "perfil");
        request.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario u = (Usuario) sesion.getAttribute("usuario");

        u.setNombres(trim(request.getParameter("nombres")));
        u.setApellidos(trim(request.getParameter("apellidos")));
        u.setEmail(trim(request.getParameter("email")));
        u.setTelefono(trim(request.getParameter("telefono")));
        u.setDireccion(trim(request.getParameter("direccion")));
        u.setCedula(trim(request.getParameter("cedula")));

        boolean ok = usuarioDao.actualizarPerfil(u);
        // Refrescar el usuario en sesión con los datos guardados
        Usuario actualizado = usuarioDao.buscarPorId(u.getIdUsuario());
        if (actualizado != null) {
            sesion.setAttribute("usuario", actualizado);
        }
        request.setAttribute("mensaje", ok ? "Datos actualizados correctamente." : "No se pudieron guardar los cambios.");
        request.setAttribute("vistaActual", "perfil");
        request.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp").forward(request, response);
    }

    private String trim(String v) {
        return (v == null) ? "" : v.trim();
    }
}
