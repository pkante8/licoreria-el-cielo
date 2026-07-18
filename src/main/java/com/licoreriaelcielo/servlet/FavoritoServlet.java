package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.List;

import com.licoreriaelcielo.dao.FavoritoDao;
import com.licoreriaelcielo.modelo.Producto;
import com.licoreriaelcielo.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de FAVORITOS: lista y alterna (agrega/quita) favoritos.
 */
@WebServlet("/favoritos")
public class FavoritoServlet extends HttpServlet {

    private final FavoritoDao favoritoDao = new FavoritoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario u = (Usuario) request.getSession().getAttribute("usuario");
        List<Producto> favoritos = favoritoDao.listarProductosFavoritos(u.getIdUsuario());
        request.setAttribute("favoritos", favoritos);
        request.setAttribute("vistaActual", "favoritos");
        request.getRequestDispatcher("/WEB-INF/jsp/favoritos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Usuario u = (Usuario) request.getSession().getAttribute("usuario");
        int idProducto = parseInt(request.getParameter("id"));
        if (idProducto > 0) {
            favoritoDao.alternar(u.getIdUsuario(), idProducto);
        }
        // Volver a la página de origen (catálogo, detalle o la propia lista).
        String volver = request.getParameter("volver");
        if (volver == null || volver.isEmpty()) {
            volver = "/favoritos";
        }
        response.sendRedirect(request.getContextPath() + volver);
    }

    private int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
