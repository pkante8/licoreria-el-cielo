<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Finalizar Compra - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />
  <c:set var="u" value="${sessionScope.usuario}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1000px; margin: 0 auto;">
        <h1 class="page-title">Finalizar Compra</h1>

        <div style="display:grid; grid-template-columns: 1fr 360px; gap:24px; align-items:start;">
          <div class="card section-card">
            <div class="card-header-title" style="margin-bottom:18px;">Datos del Cliente</div>
            <div class="form-row">
              <div class="form-group"><label class="form-label">Nombre</label>
                <input type="text" class="form-input" value="${u.nombreCompleto}" readonly></div>
              <div class="form-group"><label class="form-label">Teléfono</label>
                <input type="text" class="form-input" value="${u.telefono}" readonly></div>
            </div>
            <div class="form-group"><label class="form-label">Correo</label>
              <input type="text" class="form-input" value="${u.email}" readonly></div>
            <div class="form-group"><label class="form-label">Dirección de entrega</label>
              <input type="text" class="form-input" value="${u.direccion}" readonly></div>
            <p style="color:var(--gris-claro); font-size:13px;">
              Puedes actualizar estos datos desde <a href="${ctx}/perfil" style="color:var(--dorado-principal);">Mi Perfil</a>.
            </p>
          </div>

          <aside class="summary-card">
            <h3 class="summary-title">Resumen del Pedido</h3>
            <c:forEach var="it" items="${carrito}">
              <div class="summary-row">
                <span class="label">${it.cantidad} × ${it.producto.nombre}</span>
                <span>$ <fmt:formatNumber value="${it.subtotal}" type="number" maxFractionDigits="0" groupingUsed="true"/></span>
              </div>
            </c:forEach>
            <div class="summary-divider"></div>
            <div class="summary-total">
              <span class="label">Total</span>
              <span class="value">$ <fmt:formatNumber value="${totalCarrito}" type="number" maxFractionDigits="0" groupingUsed="true"/></span>
            </div>
            <form method="post" action="${ctx}/checkout">
              <button type="submit" class="btn btn-primary btn-block btn-lg">Confirmar Pedido</button>
            </form>
            <a href="${ctx}/carrito" class="btn btn-secondary btn-block" style="margin-top:10px;">Volver al Carrito</a>
          </aside>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
