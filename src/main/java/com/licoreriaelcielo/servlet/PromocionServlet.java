package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.List;

import com.licoreriaelcielo.dao.PromocionDao;
import com.licoreriaelcielo.modelo.Promocion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de PROMOCIONES (cliente): muestra las ofertas activas.
 */
@WebServlet("/promociones")
public class PromocionServlet extends HttpServlet {

    private final PromocionDao promocionDao = new PromocionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Promocion> promociones = promocionDao.listarActivas();
        request.setAttribute("promociones", promociones);
        request.setAttribute("vistaActual", "promociones");
        request.getRequestDispatcher("/WEB-INF/jsp/promociones.jsp").forward(request, response);
    }
}
