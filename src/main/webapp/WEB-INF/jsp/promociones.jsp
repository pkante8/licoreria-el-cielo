<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Promociones - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <h1 class="page-title">Ofertas y Promociones</h1>
        <p class="page-subtitle">Aprovecha nuestros descuentos por tiempo limitado</p>

        <c:choose>
          <c:when test="${empty promociones}">
            <div class="empty-state"><h3>No hay promociones activas</h3></div>
          </c:when>
          <c:otherwise>
            <div class="promo-grid">
              <c:forEach var="promo" items="${promociones}">
                <article class="promo-card">
                  <div class="promo-discount">
                    <c:choose>
                      <c:when test="${promo.tipo == '2x1'}">2x1</c:when>
                      <c:otherwise>-${promo.valor}%</c:otherwise>
                    </c:choose>
                  </div>
                  <div class="promo-image">
                    <svg class="bottle-svg" viewBox="0 0 60 160"><use href="#bottle"/></svg>
                  </div>
                  <h3>${promo.nombre}</h3>
                  <p class="promo-validity">Producto: ${promo.producto}</p>
                  <p class="promo-validity">Válido hasta ${promo.fechaFin}</p>
                  <a href="${ctx}/catalogo" class="btn btn-primary btn-block">Ver catálogo</a>
                </article>
              </c:forEach>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</body>
</html>
