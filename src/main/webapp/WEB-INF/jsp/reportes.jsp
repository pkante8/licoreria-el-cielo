<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Reportes - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <h1 class="page-title">Reportes</h1>
        <p class="page-subtitle">Resumen general del sistema</p>

        <div class="stats-grid">
          <div class="stat-card income"><div><div class="label">Ventas totales</div><div class="value">$ <fmt:formatNumber value="${totalVentas}" type="number" maxFractionDigits="0" groupingUsed="true"/></div></div></div>
          <div class="stat-card success"><div><div class="label">Pedidos</div><div class="value">${totalPedidos}</div></div></div>
          <div class="stat-card warning"><div><div class="label">Productos</div><div class="value">${totalProductos}</div></div></div>
          <div class="stat-card danger"><div><div class="label">Usuarios</div><div class="value">${totalUsuarios}</div></div></div>
        </div>

        <div class="inventory-table" style="margin-top:24px;">
          <div class="inventory-header">
            <h3>Detalle de pedidos</h3>
            <span class="count">${pedidos.size()} pedidos</span>
          </div>
          <c:choose>
            <c:when test="${empty pedidos}">
              <p style="color:var(--gris-claro);">Aún no hay pedidos registrados.</p>
            </c:when>
            <c:otherwise>
              <table>
                <thead>
                  <tr><th>#</th><th>Fecha</th><th>Estado · Cliente</th><th>Total</th></tr>
                </thead>
                <tbody>
                  <c:forEach var="p" items="${pedidos}">
                    <tr>
                      <td>${p.idPedido}</td>
                      <td>${p.fecha}</td>
                      <td>${p.estado}</td>
                      <td class="price-cell">$ <fmt:formatNumber value="${p.total}" type="number" maxFractionDigits="0" groupingUsed="true"/></td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
