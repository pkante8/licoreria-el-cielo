<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Licorería El Cielo - Iniciar Sesión</title>
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

<section id="login" class="screen active">
  <div class="auth-card">
    <div class="auth-header">
      <div class="logo-icon"><svg viewBox="0 0 24 24"><use href="#bag"/></svg></div>
      <h2>Licorería El Cielo</h2>
      <p>Sistema de Gestión y Ventas</p>
    </div>

    <!-- Pestañas de rol -->
    <div class="role-tabs" role="tablist">
      <button type="button" class="role-tab active" data-role="cliente" role="tab">Cliente</button>
      <button type="button" class="role-tab" data-role="admin" role="tab">Administrador</button>
    </div>

    <!-- Mensajes -->
    <c:if test="${not empty error}">
      <div class="demo-hint" style="border-color: rgba(239,68,68,0.4); color: var(--rojo-error); margin-bottom:16px;">
        ${error}
      </div>
    </c:if>
    <c:if test="${not empty exito}">
      <div class="demo-hint" style="border-color: rgba(16,185,129,0.4); color: var(--verde-exito); margin-bottom:16px;">
        ${exito}
      </div>
    </c:if>

    <div class="demo-hint" style="background: rgba(245,158,11,0.08); border:1px solid rgba(245,158,11,0.3); border-radius:8px; padding:10px 12px; margin-bottom:16px; font-size:12px; color:var(--gris-claro);">
      <strong style="color: var(--dorado-principal);">Credenciales de prueba:</strong><br>
      <span data-hint-cliente>cliente@elcielo.com / 123456</span>
      <span data-hint-admin style="display:none;">admin@elcielo.com / admin123</span>
    </div>

    <form id="login-form" method="post" action="${pageContext.request.contextPath}/login">
      <input type="hidden" name="rol" id="rol-input" value="cliente">
      <div class="form-group">
        <label class="form-label">Email o Usuario</label>
        <div class="input-wrapper">
          <input type="email" name="email" class="form-input" placeholder="correo@ejemplo.com" value="${email}">
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Contraseña</label>
        <div class="input-wrapper">
          <input type="password" name="password" id="login-password" class="form-input" placeholder="••••••••">
          <button type="button" class="input-action" data-toggle-password="login-password" aria-label="Ver contraseña">
            <svg viewBox="0 0 24 24"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" stroke-linecap="round" stroke-linejoin="round"/><circle cx="12" cy="12" r="3" stroke-linecap="round" stroke-linejoin="round"/></svg>
          </button>
        </div>
      </div>
      <button type="submit" class="btn btn-primary btn-block btn-lg" style="margin-top:8px;">Iniciar Sesión</button>
      <div class="auth-footer-link" style="margin-top: 12px;">
        ¿No tienes cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate aquí</a>
      </div>
    </form>
  </div>

  <script>
    // Sólo estado visual de la interfaz; la validación real está en el servidor.
    const rolInput = document.getElementById('rol-input');
    const hintCli = document.querySelector('[data-hint-cliente]');
    const hintAdm = document.querySelector('[data-hint-admin]');
    document.querySelectorAll('.role-tab').forEach((tab) => {
      tab.addEventListener('click', () => {
        document.querySelectorAll('.role-tab').forEach((t) => t.classList.remove('active'));
        tab.classList.add('active');
        const rol = tab.getAttribute('data-role');
        rolInput.value = rol;
        hintCli.style.display = (rol === 'admin') ? 'none' : 'inline';
        hintAdm.style.display = (rol === 'admin') ? 'inline' : 'none';
      });
    });
    document.querySelectorAll('[data-toggle-password]').forEach((btn) => {
      btn.addEventListener('click', () => {
        const input = document.getElementById(btn.getAttribute('data-toggle-password'));
        if (input) input.type = input.type === 'password' ? 'text' : 'password';
      });
    });
  </script>
</section>
</body>
</html>
