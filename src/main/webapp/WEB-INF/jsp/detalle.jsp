<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${producto.nombre} - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />

  <main>
    <section class="screen active">
      <div style="max-width: 1100px; margin: 0 auto;">
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalogo" style="margin-bottom:20px;">
          &larr; Volver al catálogo
        </a>

        <div class="product-detail-grid">
          <div class="product-detail-image">
            <span class="product-tag right">${producto.categoria}</span>
            <svg class="bottle-svg" viewBox="0 0 60 160" style="width:200px; height:auto;"><use href="#bottle"/></svg>
          </div>

          <div class="product-detail-info">
            <h1>${producto.nombre}</h1>
            <p class="product-detail-desc">${producto.descripcion}</p>

            <div class="product-detail-price-block">
              <span class="label">Precio</span>
              <div class="product-detail-price">$ <fmt:formatNumber value="${producto.precio}" type="number" maxFractionDigits="0" groupingUsed="true"/></div>
            </div>

            <div class="product-detail-meta">
              <div><span>Categoría</span><strong>${producto.categoria}</strong></div>
              <div><span>Volumen</span><strong>${producto.volumen}</strong></div>
              <div><span>Graduación</span><strong>${producto.gradoAlcohol}</strong></div>
              <div><span>Origen</span><strong>${producto.origen}</strong></div>
              <div><span>Stock disponible</span><strong>${producto.stock} unidades</strong></div>
            </div>

            <c:if test="${sessionScope.rol == 'cliente'}">
              <form method="post" action="${pageContext.request.contextPath}/carrito" class="product-detail-actions">
                <input type="hidden" name="accion" value="agregar">
                <input type="hidden" name="id" value="${producto.idProducto}">
                <button type="submit" class="btn btn-primary btn-block btn-lg">Agregar al carrito</button>
              </form>
            </c:if>
          </div>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
