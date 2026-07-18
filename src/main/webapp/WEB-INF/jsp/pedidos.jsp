<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mis Pedidos - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1000px; margin: 0 auto;">
        <h1 class="page-title">Mis Pedidos</h1>
        <p class="page-subtitle">Historial de tus compras</p>

        <c:choose>
          <c:when test="${empty pedidos}">
            <div class="empty-state">
              <h3>Aún no tienes pedidos</h3>
              <p>Cuando finalices una compra, aparecerá aquí.</p>
              <a class="btn btn-primary" href="${ctx}/catalogo">Ir al catálogo</a>
            </div>
          </c:when>
          <c:otherwise>
            <c:forEach var="ped" items="${pedidos}">
              <article class="card section-card">
                <div class="card-header">
                  <div class="card-header-title">Pedido #${ped.idPedido}</div>
                  <span class="badge badge-info">${ped.estado}</span>
                </div>
                <p style="color:var(--gris-claro); font-size:13px; margin-bottom:12px;">Fecha: ${ped.fecha}</p>
                <c:forEach var="it" items="${ped.items}">
                  <div class="order-item">
                    <div class="order-item-info">
                      <div class="order-item-name">${it.nombreProducto}</div>
                      <div class="order-item-qty">Cantidad: ${it.cantidad} × $ <fmt:formatNumber value="${it.precioUnitario}" type="number" maxFractionDigits="0" groupingUsed="true"/></div>
                    </div>
                    <div class="order-item-total">$ <fmt:formatNumber value="${it.subtotal}" type="number" maxFractionDigits="0" groupingUsed="true"/></div>
                  </div>
                </c:forEach>
                <div class="summary-total" style="margin-top:12px;">
                  <span class="label">Total</span>
                  <span class="value">$ <fmt:formatNumber value="${ped.total}" type="number" maxFractionDigits="0" groupingUsed="true"/></span>
                </div>
              </article>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</body>
</html>
