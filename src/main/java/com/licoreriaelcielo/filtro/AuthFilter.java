package com.licoreriaelcielo.filtro;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro de autenticación. Protege todas las rutas de la aplicación:
 * si no hay sesión iniciada, redirige al login. Reemplaza los "guardas
 * de ruta" que en la SPA hacía la función navigateTo() de script.js.
 *
 * Rutas públicas (sin sesión): /login, /registro y los recursos estáticos (/css).
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath.length());

        // Rutas y recursos públicos
        boolean esPublica = path.equals("/")
                || path.startsWith("/login")
                || path.startsWith("/registro")
                || path.startsWith("/css/")
                || path.equals("/index.jsp");

        HttpSession sesion = request.getSession(false);
        boolean logueado = (sesion != null && sesion.getAttribute("usuario") != null);

        if (esPublica || logueado) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(contextPath + "/login");
        }
    }
}
