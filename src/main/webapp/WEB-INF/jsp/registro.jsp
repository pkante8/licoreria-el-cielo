<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Licorería El Cielo - Registro</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<svg width="0" height="0" style="position:absolute" aria-hidden="true">
  <defs>
    <symbol id="bag" viewBox="0 0 24 24">
      <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" stroke-linejoin="round" stroke-linecap="round"/>
      <line x1="3" y1="6" x2="21" y2="6" stroke-linejoin="round" stroke-linecap="round"/>
      <path d="M16 10a4 4 0 01-8 0" stroke-linejoin="round" stroke-linecap="round"/>
    </symbol>
  </defs>
</svg>

<section id="registro" class="screen active">
  <div class="auth-card">
    <div class="auth-header">
      <div class="logo-icon"><svg viewBox="0 0 24 24"><use href="#bag"/></svg></div>
      <h2>Licorería El Cielo</h2>
      <p>Registro de Usuario</p>
    </div>

    <c:if test="${not empty error}">
      <div class="demo-hint" style="border:1px solid rgba(239,68,68,0.4); border-radius:8px; padding:10px 12px; color: var(--rojo-error); margin-bottom:16px;">
        ${error}
      </div>
    </c:if>

    <form id="registro-form" method="post" action="${pageContext.request.contextPath}/registro">
      <div class="form-row">
        <div class="form-group">
          <label class="form-label">Nombres</label>
          <input type="text" name="nombres" class="form-input" placeholder="Juan Carlos" value="${param.nombres}">
        </div>
        <div class="form-group">
          <label class="form-label">Apellidos</label>
          <input type="text" name="apellidos" class="form-input" placeholder="Pérez García" value="${param.apellidos}">
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Cédula</label>
        <input type="text" name="cedula" class="form-input" placeholder="0000000000" value="${param.cedula}">
      </div>
      <div class="form-group">
        <label class="form-label">Correo electrónico</label>
        <input type="email" name="email" class="form-input" placeholder="correo@ejemplo.com" value="${param.email}">
      </div>
      <div class="form-group">
        <label class="form-label">Contraseña</label>
        <input type="password" name="password" class="form-input" placeholder="••••••••">
      </div>
      <button type="submit" class="btn btn-primary btn-block btn-lg" style="margin-top:8px;">Guardar Usuario</button>
      <div class="auth-footer-link">
        <a href="${pageContext.request.contextPath}/login">Volver al inicio de sesión</a>
      </div>
    </form>
  </div>
</section>
</body>
</html>
