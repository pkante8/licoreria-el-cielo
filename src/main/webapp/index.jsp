<%@ page contentType="text/html; charset=UTF-8" %>
<%-- Punto de entrada: redirige al login (o a inicio si ya hay sesión). --%>
<%
    if (session != null && session.getAttribute("usuario") != null) {
        response.sendRedirect(request.getContextPath() + "/inicio");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
