package com.licoreriaelcielo.servlet;

import java.io.IOException;
import java.util.Map;

import com.licoreriaelcielo.dao.PedidoDao;
import com.licoreriaelcielo.modelo.ItemCarrito;
import com.licoreriaelcielo.modelo.Pedido;
import com.licoreriaelcielo.modelo.PedidoItem;
import com.licoreriaelcielo.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador del CHECKOUT: muestra los datos del cliente y el resumen,
 * y al confirmar crea el pedido en la base de datos y vacía el carrito.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final PedidoDao pedidoDao = new PedidoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<Integer, ItemCarrito> carrito = obtenerCarrito(request);
        if (carrito == null || carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrito");
            return;
        }
        request.setAttribute("carrito", carrito.values());
        request.setAttribute("totalCarrito", calcularTotal(carrito));
        request.setAttribute("vistaActual", "carrito");
        request.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession sesion = request.getSession();
        Map<Integer, ItemCarrito> carrito = obtenerCarrito(request);
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if (carrito == null || carrito.isEmpty() || usuario == null) {
            response.sendRedirect(request.getContextPath() + "/carrito");
            return;
        }

        // Construir el pedido a partir del carrito
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(usuario.getIdUsuario());
        pedido.setEstado("En preparación");
        double total = 0;
        for (ItemCarrito it : carrito.values()) {
            pedido.getItems().add(new PedidoItem(
                    it.getProducto().getIdProducto(),
                    it.getProducto().getNombre(),
                    it.getCantidad(),
                    it.getProducto().getPrecio()));
            total += it.getSubtotal();
        }
        pedido.setTotal(total);

        int idPedido = pedidoDao.crearPedido(pedido);
        if (idPedido > 0) {
            carrito.clear(); // vaciar carrito tras la compra
            response.sendRedirect(request.getContextPath() + "/pedidos");
        } else {
            response.sendRedirect(request.getContextPath() + "/carrito");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, ItemCarrito> obtenerCarrito(HttpServletRequest request) {
        return (Map<Integer, ItemCarrito>) request.getSession().getAttribute("carrito");
    }

    private double calcularTotal(Map<Integer, ItemCarrito> carrito) {
        double total = 0;
        for (ItemCarrito it : carrito.values()) {
            total += it.getSubtotal();
        }
        return total;
    }
}
