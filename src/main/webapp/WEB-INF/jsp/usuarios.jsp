<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Usuarios - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />

  <main>
    <section class="screen active">
      <div style="max-width: 1280px; margin: 0 auto;">
        <h1 class="page-title">Gestión de Usuarios</h1>
        <p class="page-subtitle">Administra los usuarios registrados en el sistema</p>

        <div class="table-wrap">
          <table class="data-table">
            <thead>
              <tr>
                <th>Cliente</th><th>Correo</th><th>Cédula</th><th>Rol</th><th>Estado</th><th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="u" items="${usuarios}">
                <tr>
                  <td><strong>${u.nombreCompleto}</strong></td>
                  <td>${u.email}</td>
                  <td>${u.cedula}</td>
                  <td>${u.rol}</td>
                  <td>
                    <span class="status-badge ${u.estado == 'activo' ? 'status-active' : 'status-suspended'}">${u.estado}</span>
                  </td>
                  <td>
                    <form method="post" action="${ctx}/usuarios" style="margin:0;">
                      <input type="hidden" name="accion" value="toggle">
                      <input type="hidden" name="id" value="${u.idUsuario}">
                      <button type="submit" class="btn-icon-sm">
                        ${u.estado == 'activo' ? 'Suspender' : 'Reactivar'}
                      </button>
                    </form>
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
