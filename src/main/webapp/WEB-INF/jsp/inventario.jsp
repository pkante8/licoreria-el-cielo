<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Inventario - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />

  <c:set var="ctx" value="${pageContext.request.contextPath}" />
  <c:set var="ed" value="${editando}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <h1 class="page-title">Inventario</h1>
        <p class="page-subtitle">Gestión de productos de la Licorería El Cielo</p>

        <!-- Estadísticas -->
        <div class="stats-grid">
          <div class="stat-card success">
            <div><div class="label">Total Productos</div><div class="value">${total}</div></div>
          </div>
          <div class="stat-card warning">
            <div><div class="label">Stock Bajo</div><div class="value">${stockBajo}</div></div>
          </div>
          <div class="stat-card danger">
            <div><div class="label">Agotados</div><div class="value">${agotados}</div></div>
          </div>
          <div class="stat-card income">
            <div><div class="label">Valor Total</div><div class="value">$ <fmt:formatNumber value="${valorTotal}" type="number" maxFractionDigits="0" groupingUsed="true"/></div></div>
          </div>
        </div>

        <!-- Formulario Agregar / Editar producto -->
        <div class="dashboard-card" style="margin-bottom:24px;">
          <h3 style="margin-bottom:18px;">
            <c:choose>
              <c:when test="${not empty ed}">Editar producto (#${ed.idProducto})</c:when>
              <c:otherwise>Agregar nuevo producto</c:otherwise>
            </c:choose>
          </h3>
          <form method="post" action="${ctx}/inventario">
            <input type="hidden" name="accion" value="${not empty ed ? 'actualizar' : 'agregar'}">
            <c:if test="${not empty ed}"><input type="hidden" name="id" value="${ed.idProducto}"></c:if>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Nombre</label>
                <input type="text" name="nombre" class="form-input" required value="${ed.nombre}">
              </div>
              <div class="form-group">
                <label class="form-label">Categoría</label>
                <input type="text" name="categoria" class="form-input" required value="${ed.categoria}">
              </div>
            </div>
            <div class="form-group">
              <label class="form-label">Descripción</label>
              <input type="text" name="descripcion" class="form-input" value="${ed.descripcion}">
            </div>
            <div class="form-row-3">
              <div class="form-group">
                <label class="form-label">Precio (COP)</label>
                <input type="number" step="0.01" min="0" name="precio" class="form-input" required value="${ed.precio}">
              </div>
              <div class="form-group">
                <label class="form-label">Stock</label>
                <input type="number" min="0" name="stock" class="form-input" required value="${ed.stock}">
              </div>
              <div class="form-group">
                <label class="form-label">Volumen</label>
                <input type="text" name="volumen" class="form-input" value="${ed.volumen}">
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Graduación</label>
                <input type="text" name="gradoAlcohol" class="form-input" value="${ed.gradoAlcohol}">
              </div>
              <div class="form-group">
                <label class="form-label">Origen</label>
                <input type="text" name="origen" class="form-input" value="${ed.origen}">
              </div>
            </div>
            <div style="display:flex; gap:10px;">
              <button type="submit" class="btn btn-primary">
                ${not empty ed ? 'Guardar cambios' : 'Agregar producto'}
              </button>
              <c:if test="${not empty ed}">
                <a href="${ctx}/inventario" class="btn btn-secondary">Cancelar</a>
              </c:if>
            </div>
          </form>
        </div>

        <!-- Tabla de inventario -->
        <div class="inventory-table">
          <div class="inventory-header">
            <h3>Productos en Inventario</h3>
            <span class="count">${total} productos</span>
          </div>
          <table>
            <thead>
              <tr>
                <th>Producto</th>
                <th>Categoría</th>
                <th>Stock</th>
                <th>Estado</th>
                <th>Precio</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="p" items="${productos}">
                <tr>
                  <td>${p.nombre}</td>
                  <td>${p.categoria}</td>
                  <td>
                    <c:choose>
                      <c:when test="${p.stock == 0}"><span class="stock-danger">0 unidades</span></c:when>
                      <c:when test="${p.stock < 10}"><span class="stock-warning">${p.stock} unidades</span></c:when>
                      <c:otherwise>${p.stock} unidades</c:otherwise>
                    </c:choose>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${p.stock == 0}"><span class="badge badge-danger">Agotado</span></c:when>
                      <c:when test="${p.stock < 10}"><span class="badge badge-warning">Stock Bajo</span></c:when>
                      <c:otherwise><span class="badge badge-success">Disponible</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td class="price-cell">$ <fmt:formatNumber value="${p.precio}" type="number" maxFractionDigits="0" groupingUsed="true"/></td>
                  <td>
                    <div class="table-actions">
                      <a class="btn-icon-sm" href="${ctx}/inventario?editar=${p.idProducto}">Editar</a>
                      <form method="post" action="${ctx}/inventario" style="display:inline;"
                            onsubmit="return confirm('¿Eliminar &quot;${p.nombre}&quot;?');">
                        <input type="hidden" name="accion" value="eliminar">
                        <input type="hidden" name="id" value="${p.idProducto}">
                        <button type="submit" class="btn-icon-sm danger">Eliminar</button>
                      </form>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
