<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Licorería El Cielo - Catálogo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <div class="catalog-header">
          <div class="catalog-title">
            <h2>Catálogo de Productos</h2>
            <p>Explora nuestra selección de licores premium</p>
          </div>
        </div>

        <c:choose>
          <c:when test="${empty productos}">
            <div class="empty-state">
              <h3>No hay productos en el catálogo</h3>
              <p>Aún no se han registrado productos en la base de datos.</p>
            </div>
          </c:when>
          <c:otherwise>
            <div class="product-grid">
              <c:forEach var="p" items="${productos}">
                <a class="product-card" style="display:block;" href="${pageContext.request.contextPath}/producto?id=${p.idProducto}">
                  <div class="product-image">
                    <span class="product-tag right">${p.categoria}</span>
                    <svg class="bottle-svg" viewBox="0 0 60 160"><use href="#bottle"/></svg>
                  </div>
                  <div class="product-info">
                    <h3 class="product-name">${p.nombre}</h3>
                    <p class="product-desc">${p.descripcion}</p>
                    <div class="product-bottom">
                      <div class="product-price-block">
                        <div class="label">Precio</div>
                        <div class="product-price">$ <fmt:formatNumber value="${p.precio}" type="number" maxFractionDigits="0" groupingUsed="true"/></div>
                      </div>
                    </div>
                  </div>
                </a>
              </c:forEach>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</body>
</html>
