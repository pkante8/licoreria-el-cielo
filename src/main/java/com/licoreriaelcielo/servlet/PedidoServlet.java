package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.List;

import com.licoreriaelcielo.dao.PedidoDao;
import com.licoreriaelcielo.modelo.Pedido;
import com.licoreriaelcielo.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de MIS PEDIDOS: lista los pedidos del usuario en sesión.
 */
@WebServlet("/pedidos")
public class PedidoServlet extends HttpServlet {

    private final PedidoDao pedidoDao = new PedidoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<Pedido> pedidos = pedidoDao.listarPorUsuario(usuario.getIdUsuario());
        request.setAttribute("pedidos", pedidos);
        request.setAttribute("vistaActual", "pedidos");
        request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp").forward(request, response);
    }
}
