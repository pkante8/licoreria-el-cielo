<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Promociones (Admin) - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <h1 class="page-title">Gestión de Promociones</h1>
        <p class="page-subtitle">Crea y administra las ofertas del sistema</p>

        <div class="dashboard-grid-2" style="margin-top:24px;">
          <!-- Nueva promoción -->
          <div class="dashboard-card">
            <h3 style="margin-bottom:18px;">Nueva promoción</h3>
            <form method="post" action="${ctx}/admin-promociones">
              <input type="hidden" name="accion" value="crear">
              <div class="form-group"><label class="form-label">Nombre</label>
                <input type="text" name="nombre" class="form-input" required></div>
              <div class="form-group"><label class="form-label">Producto</label>
                <input type="text" name="producto" class="form-input"></div>
              <div class="form-row">
                <div class="form-group"><label class="form-label">Tipo</label>
                  <select name="tipo" class="form-input">
                    <option value="descuento">% Descuento</option>
                    <option value="2x1">2x1</option>
                    <option value="combo">Combo</option>
                  </select></div>
                <div class="form-group"><label class="form-label">Valor (%)</label>
                  <input type="number" name="valor" class="form-input" min="0" max="100" value="0"></div>
              </div>
              <div class="form-row">
                <div class="form-group"><label class="form-label">Fecha inicio</label>
                  <input type="date" name="fechaInicio" class="form-input"></div>
                <div class="form-group"><label class="form-label">Fecha fin</label>
                  <input type="date" name="fechaFin" class="form-input"></div>
              </div>
              <button type="submit" class="btn btn-primary btn-block">Crear promoción</button>
            </form>
          </div>

          <!-- Lista de promociones -->
          <div class="dashboard-card">
            <div class="dashboard-card-head">
              <h3>Promociones</h3>
              <span class="dashboard-card-badge">${promociones.size()}</span>
            </div>
            <ul class="promo-admin-list">
              <c:forEach var="p" items="${promociones}">
                <li class="promo-admin-item">
                  <div>
                    <strong>${p.nombre}</strong>
                    <span>${p.producto} ·
                      <c:choose>
                        <c:when test="${p.tipo == 'descuento'}">${p.valor}% off</c:when>
                        <c:otherwise>${p.tipo}</c:otherwise>
                      </c:choose>
                      · hasta ${p.fechaFin}</span>
                  </div>
                  <div class="promo-admin-actions">
                    <form method="post" action="${ctx}/admin-promociones" style="margin:0;"
                          onsubmit="return confirm('¿Eliminar esta promoción?');">
                      <input type="hidden" name="accion" value="eliminar">
                      <input type="hidden" name="id" value="${p.idPromocion}">
                      <button type="submit" class="btn-icon-sm danger">Eliminar</button>
                    </form>
                  </div>
                </li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
