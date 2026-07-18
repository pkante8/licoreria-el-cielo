<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mi Perfil - Licorería El Cielo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="logged-in role-${sessionScope.rol}">
  <jsp:include page="/WEB-INF/jsp/partials/sprite.jsp" />
  <jsp:include page="/WEB-INF/jsp/partials/nav.jsp" />
  <c:set var="ctx" value="${pageContext.request.contextPath}" />
  <c:set var="u" value="${sessionScope.usuario}" />

  <main>
    <section class="screen active">
      <div style="max-width: 720px; margin: 0 auto;">
        <h1 class="page-title">Mi Perfil</h1>
        <p class="page-subtitle">Administra tus datos personales</p>

        <c:if test="${not empty mensaje}">
          <div class="demo-hint" style="border:1px solid rgba(16,185,129,0.4); border-radius:8px; padding:10px 12px; color:var(--verde-exito); margin-bottom:16px;">
            ${mensaje}
          </div>
        </c:if>

        <div class="profile-card">
          <form method="post" action="${ctx}/perfil">
            <div class="form-row">
              <div class="form-group"><label class="form-label">Nombres</label>
                <input type="text" name="nombres" class="form-input" value="${u.nombres}" required></div>
              <div class="form-group"><label class="form-label">Apellidos</label>
                <input type="text" name="apellidos" class="form-input" value="${u.apellidos}"></div>
            </div>
            <div class="form-group"><label class="form-label">Correo electrónico</label>
              <input type="email" name="email" class="form-input" value="${u.email}" required></div>
            <div class="form-row">
              <div class="form-group"><label class="form-label">Teléfono</label>
                <input type="text" name="telefono" class="form-input" value="${u.telefono}"></div>
              <div class="form-group"><label class="form-label">Cédula</label>
                <input type="text" name="cedula" class="form-input" value="${u.cedula}"></div>
            </div>
            <div class="form-group"><label class="form-label">Dirección de envío</label>
              <input type="text" name="direccion" class="form-input" value="${u.direccion}"></div>
            <button type="submit" class="btn btn-primary">Guardar cambios</button>
          </form>
        </div>
      </div>
    </section>
  </main>
</body>
</html>
