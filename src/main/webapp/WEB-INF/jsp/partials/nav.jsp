<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Barra lateral compartida para las pantallas con sesión iniciada.
     Reemplaza la navegación (sidebar) que en la SPA manejaba script.js.
     Muestra pestañas según el rol; se irá ampliando en cada fase. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="actual" value="${requestScope.vistaActual}" />

<nav class="dashboard-nav">
  <div class="dashboard-nav-inner">
    <div class="dashboard-brand">
      <div class="logo-icon logo-icon-sm">
        <svg viewBox="0 0 24 24"><use href="#bag"/></svg>
      </div>
      <div class="dashboard-brand-text">
        <strong>Licorería El Cielo</strong>
        <span class="dashboard-role-label">
          <c:choose>
            <c:when test="${sessionScope.rol == 'admin'}">Panel Administrador</c:when>
            <c:otherwise>Portal Cliente</c:otherwise>
          </c:choose>
        </span>
      </div>
    </div>

    <div class="dashboard-tabs" role="tablist">
      <c:choose>
        <%-- ====== CLIENTE ====== --%>
        <c:when test="${sessionScope.rol == 'cliente'}">
          <a class="dashboard-tab ${actual == 'catalogo' ? 'active' : ''}" href="${ctx}/catalogo">Catálogo</a>
          <a class="dashboard-tab ${actual == 'carrito' ? 'active' : ''}" href="${ctx}/carrito">Carrito</a>
          <a class="dashboard-tab ${actual == 'pedidos' ? 'active' : ''}" href="${ctx}/pedidos">Mis Pedidos</a>
        </c:when>
        <%-- ====== ADMIN ====== --%>
        <c:otherwise>
          <a class="dashboard-tab ${actual == 'inventario' ? 'active' : ''}" href="${ctx}/inventario">Inventario</a>
          <a class="dashboard-tab ${actual == 'catalogo' ? 'active' : ''}" href="${ctx}/catalogo">Catálogo</a>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="dashboard-actions">
      <a class="dashboard-logout" href="${ctx}/logout">
        <svg viewBox="0 0 24 24" width="18" height="18" style="stroke:currentColor; fill:none; stroke-width:2"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4" stroke-linecap="round" stroke-linejoin="round"/><polyline points="16 17 21 12 16 7" stroke-linecap="round" stroke-linejoin="round"/><line x1="21" y1="12" x2="9" y2="12" stroke-linecap="round"/></svg>
        <span>Salir (${sessionScope.usuario.nombreCompleto})</span>
      </a>
    </div>
  </div>
</nav>
