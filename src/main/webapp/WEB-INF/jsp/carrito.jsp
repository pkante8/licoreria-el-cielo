<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Carrito - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <div class="cart-title" style="margin-bottom:24px;">
          <h2>Carrito de Compras</h2>
          <p>${carrito.size()} producto(s) en tu carrito</p>
        </div>

        <c:choose>
          <c:when test="${empty carrito}">
            <div class="empty-state">
              <h3>Tu carrito está vacío</h3>
              <p>Agrega productos desde el catálogo.</p>
              <a class="btn btn-primary" href="${ctx}/catalogo">Ir al catálogo</a>
            </div>
          </c:when>
          <c:otherwise>
            <div class="cart-layout">
              <div class="cart-items">
                <c:forEach var="it" items="${carrito}">
                  <article class="cart-item">
                    <div class="cart-item-img"><svg class="bottle-svg" viewBox="0 0 60 160"><use href="#bottle"/></svg></div>
                    <div class="cart-item-content">
                      <div class="cart-item-top">
                        <div>
                          <h3 class="cart-item-name">${it.producto.nombre}</h3>
                          <span class="cart-item-tag">${it.producto.categoria}</span>
                        </div>
                        <form method="post" action="${ctx}/carrito" style="margin:0;">
                          <input type="hidden" name="accion" value="eliminar">
                          <input type="hidden" name="id" value="${it.producto.idProducto}">
                          <button type="submit" class="trash-btn" title="Eliminar">✕</button>
                        </form>
                      </div>
                      <div class="cart-item-bottom">
                        <div class="qty-control">
                          <form method="post" action="${ctx}/carrito" style="margin:0;">
                            <input type="hidden" name="accion" value="decrementar">
                            <input type="hidden" name="id" value="${it.producto.idProducto}">
                            <button type="submit" class="qty-btn">−</button>
                          </form>
                          <span class="qty-value">${it.cantidad}</span>
                          <form method="post" action="${ctx}/carrito" style="margin:0;">
                            <input type="hidden" name="accion" value="incrementar">
                            <input type="hidden" name="id" value="${it.producto.idProducto}">
                            <button type="submit" class="qty-btn">+</button>
                          </form>
                        </div>
                        <div class="cart-item-price-block">
                          <div class="cart-item-price-label">Subtotal</div>
                          <div class="cart-item-price">$ <fmt:formatNumber value="${it.subtotal}" type="number" maxFractionDigits="0" groupingUsed="true"/></div>
                        </div>
                      </div>
                    </div>
                  </article>
                </c:forEach>

                <form method="post" action="${ctx}/carrito" style="margin-top:10px;">
                  <input type="hidden" name="accion" value="vaciar">
                  <button type="submit" class="btn btn-secondary">Vaciar carrito</button>
                </form>
              </div>

              <aside class="summary-card">
                <h3 class="summary-title">Resumen de Compra</h3>
                <div class="summary-row"><span class="label">Subtotal</span><span>$ <fmt:formatNumber value="${totalCarrito}" type="number" maxFractionDigits="0" groupingUsed="true"/></span></div>
                <div class="summary-row"><span class="label">Envío</span><span>Gratis</span></div>
                <div class="summary-divider"></div>
                <div class="summary-total">
                  <span class="label">Total</span>
                  <span class="value">$ <fmt:formatNumber value="${totalCarrito}" type="number" maxFractionDigits="0" groupingUsed="true"/></span>
                </div>
                <a href="${ctx}/checkout" class="btn btn-primary btn-block btn-lg">Finalizar Compra</a>
                <a href="${ctx}/catalogo" class="btn-link">Continuar comprando</a>
              </aside>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </section>
  </main>
</body>
</html>
