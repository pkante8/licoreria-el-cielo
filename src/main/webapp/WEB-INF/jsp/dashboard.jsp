<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Dashboard - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <h1 class="page-title">Panel de Control</h1>
        <p class="page-subtitle">Resumen general de la Licorería El Cielo</p>

        <div class="kpi-grid">
          <div class="kpi-card">
            <div class="kpi-icon kpi-icon-success">$</div>
            <div>
              <div class="kpi-label">Ventas totales</div>
              <div class="kpi-value">$ <fmt:formatNumber value="${totalVentas}" type="number" maxFractionDigits="0" groupingUsed="true"/></div>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-icon kpi-icon-info">#</div>
            <div>
              <div class="kpi-label">Pedidos</div>
              <div class="kpi-value">${totalPedidos}</div>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-icon kpi-icon-warning">!</div>
            <div>
              <div class="kpi-label">Stock bajo / agotado</div>
              <div class="kpi-value">${stockBajo}</div>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-icon kpi-icon-primary">U</div>
            <div>
              <div class="kpi-label">Usuarios</div>
              <div class="kpi-value">${totalUsuarios}</div>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-icon kpi-icon-primary">P</div>
            <div>
              <div class="kpi-label">Productos</div>
              <div class="kpi-value">${totalProductos}</div>
            </div>
          </div>
        </div>

        <div class="dashboard-card" style="margin-top:24px;">
          <div class="dashboard-card-head"><h3>Accesos rápidos</h3></div>
          <div style="display:flex; gap:10px; flex-wrap:wrap;">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/inventario">Inventario</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin-promociones">Promociones</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/reportes">Reportes</a>
          </div>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
