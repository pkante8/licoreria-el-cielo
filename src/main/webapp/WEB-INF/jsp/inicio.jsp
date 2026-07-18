<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Licorería El Cielo - Inicio</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<section class="screen active">
  <div style="max-width: 720px; margin: 0 auto;">
    <div class="auth-card" style="text-align:center;">
      <div class="auth-header">
        <h2>¡Bienvenido, ${sessionScope.usuario.nombreCompleto}!</h2>
        <p>Has iniciado sesión como
          <strong style="color:var(--dorado-principal);">${sessionScope.rol}</strong>.
        </p>
      </div>

      <p style="color:var(--gris-claro); margin-bottom:24px;">
        La autenticación funciona correctamente. En las próximas fases, desde aquí
        <c:choose>
          <c:when test="${sessionScope.rol == 'admin'}">
            entrarás al <strong>Panel de Administración</strong> (dashboard, inventario, usuarios, reportes).
          </c:when>
          <c:otherwise>
            entrarás al <strong>Catálogo</strong> para explorar y comprar productos.
          </c:otherwise>
        </c:choose>
      </p>

      <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-block btn-lg">
        Cerrar sesión
      </a>
    </div>
  </div>
</section>
</body>
</html>
