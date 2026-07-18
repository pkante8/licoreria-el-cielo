package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.licoreriaelcielo.dao.ProductoDao;
import com.licoreriaelcielo.modelo.ItemCarrito;
import com.licoreriaelcielo.modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador del CARRITO de compras. El carrito vive en la sesión
 * (un mapa idProducto -> ItemCarrito). Reemplaza la lógica de carrito
 * que en la SPA estaba en script.js.
 */
@WebServlet("/carrito")
public class CarritoServlet extends HttpServlet {

    private final ProductoDao productoDao = new ProductoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<Integer, ItemCarrito> carrito = obtenerCarrito(request);
        request.setAttribute("carrito", carrito.values());
        request.setAttribute("totalCarrito", calcularTotal(carrito));
        request.setAttribute("vistaActual", "carrito");
        request.getRequestDispatcher("/WEB-INF/jsp/carrito.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<Integer, ItemCarrito> carrito = obtenerCarrito(request);
        String accion = request.getParameter("accion");
        int id = parseInt(request.getParameter("id"));

        switch (accion == null ? "" : accion) {
            case "agregar":
                agregar(carrito, id);
                break;
            case "incrementar":
                if (carrito.containsKey(id)) {
                    ItemCarrito it = carrito.get(id);
                    it.setCantidad(it.getCantidad() + 1);
                }
                break;
            case "decrementar":
                if (carrito.containsKey(id)) {
                    ItemCarrito it = carrito.get(id);
                    if (it.getCantidad() > 1) {
                        it.setCantidad(it.getCantidad() - 1);
                    }
                }
                break;
            case "eliminar":
                carrito.remove(id);
                break;
            case "vaciar":
                carrito.clear();
                break;
            default:
                break;
        }
        response.sendRedirect(request.getContextPath() + "/carrito");
    }

    private void agregar(Map<Integer, ItemCarrito> carrito, int id) {
        if (carrito.containsKey(id)) {
            ItemCarrito it = carrito.get(id);
            it.setCantidad(it.getCantidad() + 1);
        } else {
            Producto p = productoDao.buscarProductoPorId(id);
            if (p != null) {
                carrito.put(id, new ItemCarrito(p, 1));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, ItemCarrito> obtenerCarrito(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        Map<Integer, ItemCarrito> carrito = (Map<Integer, ItemCarrito>) sesion.getAttribute("carrito");
        if (carrito == null) {
            carrito = new LinkedHashMap<>();
            sesion.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private double calcularTotal(Map<Integer, ItemCarrito> carrito) {
        double total = 0;
        for (ItemCarrito it : carrito.values()) {
            total += it.getSubtotal();
        }
        return total;
    }

    private int parseInt(String v) {
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
