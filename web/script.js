/* =========================================================
   LICORERÍA EL CIELO - JavaScript principal
   Sistema SPA: navegación entre pantallas + validaciones + modales
   ========================================================= */

document.addEventListener('DOMContentLoaded', () => {

  // =========================================================
  // CREDENCIALES DE PRUEBA (con rol)
  // En producción esto vendría de un backend.
  // ROLES: 'cliente' o 'admin'
  // =========================================================
  const usuariosRegistrados = [
    { email: 'cliente@elcielo.com', password: '123456',  rol: 'cliente' },
    { email: 'admin@elcielo.com',   password: 'admin123', rol: 'admin' },
  ];

  let usuarioActual = null;
  let rolSeleccionado = 'cliente'; // pestaña activa por defecto


  // =========================================================
  // PESTAÑAS DE ROL (Cliente / Administrador)
  // =========================================================
  const roleTabs = document.querySelectorAll('.role-tab');
  const hintCliente = document.querySelector('[data-hint-cliente]');
  const hintAdmin   = document.querySelector('[data-hint-admin]');

  roleTabs.forEach((tab) => {
    tab.addEventListener('click', () => {
      roleTabs.forEach((t) => {
        t.classList.remove('active');
        t.setAttribute('aria-selected', 'false');
      });
      tab.classList.add('active');
      tab.setAttribute('aria-selected', 'true');
      rolSeleccionado = tab.getAttribute('data-role');

      // Actualiza la pista de credenciales según el rol
      if (hintCliente && hintAdmin) {
        if (rolSeleccionado === 'admin') {
          hintCliente.style.display = 'none';
          hintAdmin.style.display   = 'inline';
        } else {
          hintCliente.style.display = 'inline';
          hintAdmin.style.display   = 'none';
        }
      }
    });
  });


  /**
   * Aplica la clase de rol al body para mostrar/ocultar
   * elementos marcados con .cliente-only o .admin-only.
   * Si rol es null, también remueve la clase .logged-in.
   */
  function aplicarRolAlBody(rol) {
    document.body.classList.remove('role-cliente', 'role-admin');
    if (rol) {
      document.body.classList.add(`role-${rol}`);
      document.body.classList.add('logged-in');
    } else {
      document.body.classList.remove('logged-in');
    }
  }

  /**
   * Marca como activa la pestaña del dashboard que coincide
   * con la pantalla actualmente visible.
   */
  function actualizarPestanaActiva(screenId) {
    document.querySelectorAll('.dashboard-tab').forEach((tab) => {
      const target = tab.getAttribute('data-nav');
      if (target === screenId) {
        tab.classList.add('active');
        tab.setAttribute('aria-selected', 'true');
      } else {
        tab.classList.remove('active');
        tab.setAttribute('aria-selected', 'false');
      }
    });
  }


  // =========================================================
  // ROUTER: Navegación entre pantallas
  // =========================================================
  const screens = document.querySelectorAll('.screen');

  /**
   * Muestra la pantalla con el ID dado y oculta las demás.
   * @param {string} screenId - ID de la pantalla destino.
   */
  function navigateTo(screenId) {
    // Pantallas que requieren estar logueado
    const pantallasProtegidas = ['catalogo', 'carrito', 'datos-cliente', 'pedidos', 'inventario',
                                 'perfil', 'favoritos', 'promociones', 'detalle-producto',
                                 'dashboard-admin', 'usuarios', 'admin-promociones', 'reportes'];
    // Pantallas exclusivas por rol
    const soloCliente = ['carrito', 'datos-cliente', 'perfil', 'favoritos', 'promociones', 'detalle-producto'];
    const soloAdmin   = ['inventario', 'dashboard-admin', 'usuarios', 'admin-promociones', 'reportes'];
    // Pantallas que NO se deben mostrar si ya hay sesión
    const pantallasPublicas = ['login', 'registro'];

    if (usuarioActual) {
      // Si ya hay sesión y se intenta entrar a login/registro,
      // redirigimos a la pantalla principal del rol.
      if (pantallasPublicas.includes(screenId)) {
        screenId = (usuarioActual.rol === 'admin') ? 'dashboard-admin' : 'catalogo';
      }
      // Si un cliente intenta entrar a una pantalla solo-admin — catálogo
      if (usuarioActual.rol === 'cliente' && soloAdmin.includes(screenId)) {
        screenId = 'catalogo';
      }
      // Si un admin intenta entrar a una pantalla solo-cliente — dashboard
      if (usuarioActual.rol === 'admin' && soloCliente.includes(screenId)) {
        screenId = 'dashboard-admin';
      }
    } else {
      // Sin sesión: cualquier pantalla protegida redirige al login
      if (pantallasProtegidas.includes(screenId)) {
        screenId = 'login';
      }
    }

    screens.forEach((s) => s.classList.remove('active'));
    const target = document.getElementById(screenId);
    if (target) {
      target.classList.add('active');
      window.scrollTo({ top: 0, behavior: 'instant' });
      // Actualizamos el hash sin disparar el evento hashchange
      history.replaceState(null, '', `#${screenId}`);

      // Marcamos la pestaña correspondiente.
      // 'datos-cliente' pertenece al flujo del carrito.
      const tabId = (screenId === 'datos-cliente') ? 'carrito' : screenId;
      actualizarPestanaActiva(tabId);

      // Re-renderizar contenido dinámico al entrar a cada pantalla.
      // Esto garantiza que los datos del admin siempre se vean,
      // sin importar el orden de carga.
      if (screenId === 'dashboard-admin' && typeof actualizarDashboard === 'function') {
        actualizarDashboard();
      } else if (screenId === 'usuarios' && typeof renderizarUsuarios === 'function') {
        renderizarUsuarios();
      } else if (screenId === 'admin-promociones' && typeof renderizarPromocionesAdmin === 'function') {
        renderizarPromocionesAdmin();
      }
    } else {
      // Si la pantalla no existe, vamos al login
      document.getElementById('login').classList.add('active');
    }
  }

  // Interceptamos todos los clicks en enlaces internos (href="#algo")
  // y en botones con data-nav="algo"
  document.addEventListener('click', (e) => {
    // Enlaces tipo <a href="#pantalla">
    const link = e.target.closest('a[href^="#"]');
    if (link) {
      const href = link.getAttribute('href');
      if (href && href.length > 1 && href !== '#') {
        e.preventDefault();
        const screenId = href.substring(1);
        navigateTo(screenId);
        return;
      }
    }

    // Botones con data-nav="pantalla"
    const navBtn = e.target.closest('[data-nav]');
    if (navBtn) {
      e.preventDefault();
      const screenId = navBtn.getAttribute('data-nav');
      // Pequeño delay si también cierra un modal, para que se vea la transición
      const cierraModal = navBtn.hasAttribute('data-close-modal');
      setTimeout(() => navigateTo(screenId), cierraModal ? 200 : 0);
    }
  });

  // Si el usuario llega con un hash en la URL, navegar a esa pantalla
  // (respetando las pantallas protegidas).
  function navegarSegunHash() {
    const hash = window.location.hash.substring(1);
    if (hash) {
      navigateTo(hash);
    } else {
      navigateTo('login');
    }
  }
  navegarSegunHash();
  window.addEventListener('hashchange', navegarSegunHash);


  // =========================================================
  // MANEJO DE MODALES
  // =========================================================
  function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    modal.classList.add('active');
    modal.setAttribute('aria-hidden', 'false');
    document.body.style.overflow = 'hidden';
  }

  function hideModal(modalEl) {
    if (!modalEl) return;
    modalEl.classList.remove('active');
    modalEl.setAttribute('aria-hidden', 'true');
    document.body.style.overflow = '';
  }

  // Cerrar modal con cualquier botón [data-close-modal]
  document.querySelectorAll('[data-close-modal]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const modal = btn.closest('.modal-overlay');
      hideModal(modal);
    });
  });

  // Cerrar modal al hacer click fuera del card
  document.querySelectorAll('.modal-overlay').forEach((overlay) => {
    overlay.addEventListener('click', (e) => {
      if (e.target === overlay) hideModal(overlay);
    });
  });

  // Cerrar modal con la tecla Escape
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      const activeModal = document.querySelector('.modal-overlay.active');
      if (activeModal) hideModal(activeModal);
    }
  });

  // Exponer funciones globalmente
  window.showModal = showModal;
  window.hideModal = hideModal;
  window.navigateTo = navigateTo;


  // =========================================================
  // 1. Toggle de visibilidad de contraseña
  // =========================================================
  document.querySelectorAll('[data-toggle-password]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const inputId = btn.getAttribute('data-toggle-password');
      const input = document.getElementById(inputId);
      if (!input) return;
      input.type = input.type === 'password' ? 'text' : 'password';
    });
  });


  // =========================================================
  // 2. LOGIN: validación + autenticación
  // =========================================================
  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const email = document.getElementById('login-email').value.trim();
      const password = document.getElementById('login-password').value.trim();

      // Campos vacíos
      if (!email || !password) {
        showModal('modal-campos');
        return;
      }

      // Formato de correo inválido
      const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
      if (!emailValido) {
        showModal('modal-correo');
        return;
      }

      // ¿Existe el usuario y coincide la contraseña?
      const usuario = usuariosRegistrados.find(
        (u) => u.email.toLowerCase() === email.toLowerCase() && u.password === password
      );

      if (!usuario) {
        showModal('modal-credenciales');
        return;
      }

      // ¿El rol del usuario coincide con la pestaña seleccionada?
      // Si alguien marca "Administrador" e intenta entrar con cuenta
      // de cliente (o viceversa), bloqueamos con el modal de acceso.
      if (usuario.rol !== rolSeleccionado) {
        showModal('modal-acceso');
        return;
      }

      // — Login exitoso — entramos a la pantalla correspondiente al rol
      usuarioActual = usuario;
      aplicarRolAlBody(usuario.rol);
      loginForm.reset();

      if (usuario.rol === 'admin') {
        // Re-renderizar todos los módulos del admin antes de mostrar
        // (por si algo no se inicializó correctamente al cargar)
        try {
          if (typeof renderizarUsuarios === 'function') renderizarUsuarios();
          if (typeof renderizarPromocionesAdmin === 'function') renderizarPromocionesAdmin();
          if (typeof actualizarDashboard === 'function') actualizarDashboard();
          console.log('✓ Módulos del admin renderizados');
        } catch (err) {
          console.error('✗ Error al renderizar admin:', err);
        }
        navigateTo('dashboard-admin');
      } else {
        navigateTo('catalogo');
      }
    });
  }

  // Link "¿Olvidaste tu contraseña?"
  const forgotLink = document.getElementById('forgot-password-link');
  if (forgotLink) {
    forgotLink.addEventListener('click', (e) => {
      e.preventDefault();
      showModal('modal-credenciales');
    });
  }


  // =========================================================
  // 3. REGISTRO: guardar nuevo usuario y volver al login
  // =========================================================
  const registroForm = document.getElementById('registro-form');
  if (registroForm) {
    registroForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const inputs = registroForm.querySelectorAll('input');
      let todosLlenos = true;

      inputs.forEach((input) => {
        if (!input.value.trim()) todosLlenos = false;
      });

      if (!todosLlenos) {
        showModal('modal-campos');
        return;
      }

      // Como el formulario actual no tiene email/password, generamos
      // unas credenciales basadas en la cédula para que el usuario
      // pueda probar el flujo. En producción aquí iría el guardado real.
      const cedula = inputs[3]?.value.trim() || 'usuario';
      const nuevoEmail = `${cedula}@elcielo.com`;
      const nuevaPassword = cedula;

      // Los registros desde la pantalla pública siempre son clientes.
      // (Los admins se crearían desde un panel interno.)
      usuariosRegistrados.push({ email: nuevoEmail, password: nuevaPassword, rol: 'cliente' });

      alert(
        `— Usuario registrado exitosamente.\n\n` +
        `Para probar tu nueva cuenta usa:\n` +
        `Email: ${nuevoEmail}\n` +
        `Contraseña: ${nuevaPassword}\n\n` +
        `(Asegúrate de seleccionar la pestaña "Cliente" al iniciar sesión)`
      );

      registroForm.reset();
      navigateTo('login');
    });
  }


  // =========================================================
  // 4. CHECKOUT: confirmar pedido
  // =========================================================
  const checkoutForm = document.getElementById('checkout-form');
  if (checkoutForm) {
    checkoutForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const inputs = checkoutForm.querySelectorAll('input[required]');
      let todosLlenos = true;

      inputs.forEach((input) => {
        if (!input.value.trim()) todosLlenos = false;
      });

      if (!todosLlenos) {
        showModal('modal-campos');
        return;
      }

      const emailInput = document.getElementById('checkout-email');
      if (emailInput) {
        const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailInput.value.trim());
        if (!emailValido) {
          showModal('modal-correo');
          return;
        }
      }

      // — Pedido confirmado — vamos a la pantalla de pedidos
      alert('✓ Pedido confirmado correctamente');
      navigateTo('pedidos');
    });
  }


  // =========================================================
  // 5. Controles +/- de cantidad en el carrito
  // =========================================================
  document.querySelectorAll('.cart-item').forEach((item) => {
    const qtyValue = item.querySelector('.qty-value');
    const buttons = item.querySelectorAll('.qty-btn');
    if (!qtyValue || buttons.length < 2) return;

    const [minusBtn, plusBtn] = buttons;

    minusBtn.addEventListener('click', () => {
      const current = parseInt(qtyValue.textContent, 10);
      if (current > 1) qtyValue.textContent = current - 1;
    });

    plusBtn.addEventListener('click', () => {
      const current = parseInt(qtyValue.textContent, 10);
      qtyValue.textContent = current + 1;
    });
  });


  // =========================================================
  // 6. Eliminar producto del carrito
  // =========================================================
  document.querySelectorAll('.trash-btn').forEach((btn) => {
    btn.addEventListener('click', () => {
      const cartItem = btn.closest('.cart-item');
      if (!cartItem) return;
      if (confirm('¿Estás seguro de eliminar este producto del carrito?')) {
        cartItem.style.transition = 'opacity 0.3s, transform 0.3s';
        cartItem.style.opacity = '0';
        cartItem.style.transform = 'translateX(20px)';
        setTimeout(() => cartItem.remove(), 300);
      }
    });
  });


  // =========================================================
  // 7. Botón "Agregar" del catálogo + contador del carrito
  // =========================================================
  document.querySelectorAll('.btn-add').forEach((btn) => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      const originalText = btn.innerHTML;
      btn.innerHTML = '✓ Agregado';
      btn.style.background = '#10B981';
      btn.style.color = '#FFFFFF';

      setTimeout(() => {
        btn.innerHTML = originalText;
        btn.style.background = '';
        btn.style.color = '';
      }, 1200);

      incrementarContadorCarrito();
    });
  });

  function incrementarContadorCarrito() {
    document.querySelectorAll('.cart-badge').forEach((badge) => {
      const current = parseInt(badge.textContent, 10) || 0;
      badge.textContent = current + 1;
    });
  }


  // =========================================================
  // 8. Logout (botones con data-logout)
  // =========================================================
  document.querySelectorAll('[data-logout]').forEach((btn) => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      if (!confirm('¿Estás seguro de cerrar sesión?')) return;
      usuarioActual = null;
      aplicarRolAlBody(null);
      navigateTo('login');
    });
  });


  // =========================================================
  // 9. NUEVOS MÓDULOS: catálogo de productos como datos
  //    (para favoritos y detalle de producto)
  // =========================================================
  const productos = [
    { id: 1, nombre: 'Whisky Premium',     desc: 'Whisky escocés añejado 12 años, notas ahumadas y especiadas',     precio: 360000, tag: 'Whisky',    rating: 4.8, vol: '750 ml', grado: '40%',  origen: 'Escocia',
      imagen:    'https://images.unsplash.com/photo-1527281400683-1aae777175f8?w=500&auto=format&fit=crop&q=70',
      imagenAlt: 'https://images.pexels.com/photos/602750/pexels-photo-602750.jpeg?auto=compress&cs=tinysrgb&w=500',
      color: '#b8860b' },
    { id: 2, nombre: 'Vino Tinto Reserva', desc: 'Vino tinto de cuerpo completo con toques de cereza y vainilla',   precio: 182000, tag: 'Vino',      rating: 4.6, vol: '750 ml', grado: '13.5%', origen: 'Argentina',
      imagen:    'https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?w=500&auto=format&fit=crop&q=70',
      imagenAlt: 'https://images.pexels.com/photos/2647947/pexels-photo-2647947.jpeg?auto=compress&cs=tinysrgb&w=500',
      color: '#722f37' },
    { id: 3, nombre: 'Vodka Importado',    desc: 'Vodka destilado cinco veces para una pureza excepcional',         precio: 140000, tag: 'Vodka',     rating: 4.4, vol: '750 ml', grado: '40%',  origen: 'Rusia',
      imagen:    'https://images.unsplash.com/photo-1608885898957-a559228e8749?w=500&auto=format&fit=crop&q=70',
      imagenAlt: 'https://images.pexels.com/photos/1283219/pexels-photo-1283219.jpeg?auto=compress&cs=tinysrgb&w=500',
      color: '#e0e0e0' },
    { id: 4, nombre: 'Ron Añejo',          desc: 'Ron añejado siete años en barricas de roble',                     precio: 212000, tag: 'Ron',       rating: 4.7, vol: '750 ml', grado: '40%',  origen: 'Cuba',
      imagen:    'https://images.unsplash.com/photo-1582819509237-d6c8df3e26c5?w=500&auto=format&fit=crop&q=70',
      imagenAlt: 'https://images.pexels.com/photos/3407777/pexels-photo-3407777.jpeg?auto=compress&cs=tinysrgb&w=500',
      color: '#5d2e0f' },
    { id: 5, nombre: 'Tequila Reposado',   desc: '100% agave azul, reposado en barricas durante seis meses',        precio: 196000, tag: 'Tequila',   rating: 4.5, vol: '750 ml', grado: '38%',  origen: 'México',
      imagen:    'https://images.unsplash.com/photo-1516535794938-6063878f08cc?w=500&auto=format&fit=crop&q=70',
      imagenAlt: 'https://images.pexels.com/photos/5947019/pexels-photo-5947019.jpeg?auto=compress&cs=tinysrgb&w=500',
      color: '#d4a017' },
    { id: 6, nombre: 'Champagne Brut',     desc: 'Champagne francés con burbujas finas y persistentes',             precio: 380000, tag: 'Champagne', rating: 4.9, vol: '750 ml', grado: '12%',  origen: 'Francia',
      imagen:    'https://images.unsplash.com/photo-1547595628-c61a29f496f0?w=500&auto=format&fit=crop&q=70',
      imagenAlt: 'https://images.pexels.com/photos/2531188/pexels-photo-2531188.jpeg?auto=compress&cs=tinysrgb&w=500',
      color: '#f5d76e' },
  ];

  /**
   * Formatea un número como precio en pesos colombianos.
   * Ej: 360000 — "$ 360.000"
   */
  function formatearCOP(valor) {
    return '$ ' + Math.round(valor).toLocaleString('es-CO');
  }


  // =========================================================
  // 10. INYECTAR botón de favorito + IMAGEN REAL en cada card
  //     del catálogo y data-product-id para identificarlas.
  // =========================================================
  const productCards = document.querySelectorAll('#catalogo .product-card');
  productCards.forEach((card, index) => {
    const producto = productos[index];
    if (!producto) return;
    card.setAttribute('data-product-id', producto.id);
    card.style.cursor = 'pointer';

    const imageDiv = card.querySelector('.product-image');
    if (!imageDiv) return;

    // Reemplazar el SVG decorativo por la imagen real del producto
    const svg = imageDiv.querySelector('.bottle-svg');
    if (svg && producto.imagen) {
      const img = document.createElement('img');
      img.src = producto.imagen;
      img.alt = producto.nombre;
      img.className = 'product-img-real';
      img.loading = 'lazy';
      // Sistema de fallback de dos niveles:
      // 1. Si la URL principal falla, probar la URL alternativa.
      // 2. Si esa también falla, usar el placeholder generado.
      let intentoFallback = 0;
      img.onerror = function () {
        if (intentoFallback === 0 && producto.imagenAlt) {
          intentoFallback = 1;
          img.src = producto.imagenAlt;
        } else {
          intentoFallback = 2;
          img.src = generarPlaceholder(producto);
          img.onerror = null;
        }
      };
      svg.replaceWith(img);
    }

    // Insertar botón de favorito en la imagen
    if (!imageDiv.querySelector('.fav-btn')) {
      const favBtn = document.createElement('button');
      favBtn.className = 'fav-btn cliente-only';
      favBtn.setAttribute('data-fav-id', producto.id);
      favBtn.setAttribute('aria-label', 'Marcar como favorito');
      favBtn.innerHTML = '<svg viewBox="0 0 24 24"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z" stroke-linecap="round" stroke-linejoin="round"/></svg>';
      imageDiv.appendChild(favBtn);
    }
  });


  // =========================================================
  // 10b. INYECTAR IMÁGENES en otras secciones
  //      (carrito, pedidos, promociones) — por orden de aparición
  // =========================================================
  function reemplazarSvgPorImagen(svg, producto, claseExtra) {
    if (!svg || !producto) return;
    const img = document.createElement('img');
    img.src = producto.imagen;
    img.alt = producto.nombre;
    img.className = 'product-img-real ' + (claseExtra || '');
    img.loading = 'lazy';
    // Fallback en cadena: imagen — imagenAlt — placeholder generado
    let intentoFallback = 0;
    img.onerror = function () {
      if (intentoFallback === 0 && producto.imagenAlt) {
        intentoFallback = 1;
        img.src = producto.imagenAlt;
      } else {
        intentoFallback = 2;
        img.src = generarPlaceholder(producto);
        img.onerror = null;
      }
    };
    svg.replaceWith(img);
  }

  /**
   * Genera un SVG inline (data URL) con el color del producto y su nombre.
   * Sirve como fallback cuando la imagen externa no carga.
   */
  function generarPlaceholder(producto) {
    const color = producto.color || '#b8860b';
    const nombre = producto.nombre.replace(/&/g, '&amp;').replace(/</g, '&lt;');
    const svg = `
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 400 400">
        <defs>
          <linearGradient id="g" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stop-color="${color}" stop-opacity="0.6"/>
            <stop offset="100%" stop-color="${color}" stop-opacity="0.2"/>
          </linearGradient>
        </defs>
        <rect width="400" height="400" fill="#0B1426"/>
        <rect x="140" y="60" width="120" height="280" rx="20" fill="url(#g)" stroke="${color}" stroke-width="2"/>
        <rect x="170" y="40" width="60" height="40" rx="4" fill="${color}" opacity="0.7"/>
        <rect x="160" y="170" width="80" height="100" rx="4" fill="rgba(0,0,0,0.3)" stroke="${color}" stroke-width="1" opacity="0.8"/>
        <text x="200" y="225" text-anchor="middle" fill="#fff" font-family="Arial,sans-serif" font-size="14" font-weight="700">${nombre}</text>
      </svg>
    `;
    return 'data:image/svg+xml;utf8,' + encodeURIComponent(svg.trim());
  }

  // Carrito: 3 productos en este orden — Whisky, Vino, Vodka
  const cartSvgs = document.querySelectorAll('#carrito .cart-item-img .bottle-svg');
  const cartProductos = [productos[0], productos[1], productos[2]];
  cartSvgs.forEach((svg, i) => reemplazarSvgPorImagen(svg, cartProductos[i], 'product-img-thumb'));

  // Pedidos: 3 productos — Whisky, Vino, Vodka
  const orderSvgs = document.querySelectorAll('#pedidos .order-item-img .bottle-svg');
  orderSvgs.forEach((svg, i) => reemplazarSvgPorImagen(svg, cartProductos[i], 'product-img-thumb'));

  // Promociones: 4 productos — Whisky, Vino, Vodka, Tequila
  const promoSvgs = document.querySelectorAll('#promociones .promo-image .bottle-svg');
  const promoProductos = [productos[0], productos[1], productos[2], productos[4]];
  promoSvgs.forEach((svg, i) => reemplazarSvgPorImagen(svg, promoProductos[i], 'product-img-promo'));

  // Banner de promoción
  const bannerSvg = document.querySelector('#promociones .promo-banner-icon svg');
  if (bannerSvg) reemplazarSvgPorImagen(bannerSvg, productos[1], 'product-img-banner');


  // =========================================================
  // 11. FAVORITOS - lógica completa
  // =========================================================
  let favoritos = []; // array de IDs de productos favoritos

  function toggleFavorito(productoId) {
    const idx = favoritos.indexOf(productoId);
    const producto = productos.find((p) => p.id === productoId);
    if (idx >= 0) {
      favoritos.splice(idx, 1);
      agregarNotificacion('Quitado de favoritos', `${producto?.nombre || 'Producto'} ya no está en tus favoritos.`);
    } else {
      favoritos.push(productoId);
      agregarNotificacion('Añadido a favoritos', `${producto?.nombre || 'Producto'} se guardó en tus favoritos.`);
    }
    actualizarVistaFavoritos();
  }

  function actualizarVistaFavoritos() {
    // Actualizar botones de corazón
    document.querySelectorAll('.fav-btn[data-fav-id]').forEach((btn) => {
      const id = parseInt(btn.getAttribute('data-fav-id'), 10);
      btn.classList.toggle('active', favoritos.includes(id));
    });

    // Actualizar badge en pestaña
    const favBadge = document.querySelector('.fav-badge');
    if (favBadge) favBadge.textContent = favoritos.length;

    // Actualizar contador en perfil
    const profileFavsCount = document.getElementById('profile-favs-count');
    if (profileFavsCount) profileFavsCount.textContent = favoritos.length;

    // Actualizar grid de favoritos
    const grid = document.getElementById('favoritos-grid');
    const empty = document.getElementById('favoritos-empty');
    if (!grid || !empty) return;

    if (favoritos.length === 0) {
      grid.style.display = 'none';
      empty.style.display = 'block';
    } else {
      grid.style.display = '';
      empty.style.display = 'none';
      grid.innerHTML = favoritos.map((id) => {
        const p = productos.find((x) => x.id === id);
        if (!p) return '';
        return `
          <article class="product-card" data-product-id="${p.id}" style="cursor:pointer;">
            <div class="product-image">
              <button class="fav-btn active" data-fav-id="${p.id}" aria-label="Quitar de favoritos">
                <svg viewBox="0 0 24 24"><path d="M20.84 4.61a5.5 5.5 0 00-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 00-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 000-7.78z" stroke-linecap="round" stroke-linejoin="round"/></svg>
              </button>
              <span class="product-tag right">${p.tag}</span>
              <img src="${p.imagen}" alt="${p.nombre}" class="product-img-real" loading="lazy">
            </div>
            <div class="product-info">
              <h3 class="product-name">${p.nombre}</h3>
              <p class="product-desc">${p.desc}</p>
              <div class="product-bottom">
                <div class="product-price-block">
                  <div class="label">Precio</div>
                  <div class="product-price">${formatearCOP(p.precio)}</div>
                </div>
                <button class="btn-add">
                  <svg viewBox="0 0 24 24" style="stroke:currentColor; fill:none; stroke-width:2"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 002 1.61h9.72a2 2 0 002-1.61L23 6H6"/></svg>
                  Agregar
                </button>
              </div>
            </div>
          </article>
        `;
      }).join('');
    }
  }

  // Click en cualquier botón de favorito (delegado al document)
  document.addEventListener('click', (e) => {
    const favBtn = e.target.closest('.fav-btn');
    if (favBtn) {
      e.preventDefault();
      e.stopPropagation();
      const id = parseInt(favBtn.getAttribute('data-fav-id'), 10);
      if (id) toggleFavorito(id);
    }
  });

  // Click en card de producto — abre detalle
  document.addEventListener('click', (e) => {
    const card = e.target.closest('.product-card[data-product-id]');
    if (!card) return;
    // Si el click fue sobre un botón dentro de la card, no abrir detalle
    if (e.target.closest('button, .btn-add, .fav-btn')) return;
    const id = parseInt(card.getAttribute('data-product-id'), 10);
    abrirDetalleProducto(id);
  });


  // =========================================================
  // 12. DETALLE DE PRODUCTO
  // =========================================================
  let productoActual = null;

  function abrirDetalleProducto(productoId) {
    const p = productos.find((x) => x.id === productoId);
    if (!p) return;
    productoActual = p;

    document.getElementById('detail-name').textContent = p.nombre;
    document.getElementById('detail-desc').textContent = p.desc;
    document.getElementById('detail-tag').textContent = p.tag;
    document.getElementById('detail-price').textContent = formatearCOP(p.precio);
    document.getElementById('detail-rating-text').textContent = `${p.rating} (124 reseñas)`;

    // Reemplazar imagen del detalle
    const detailImageBox = document.querySelector('.product-detail-image');
    if (detailImageBox && p.imagen) {
      // Quitar SVG o img anterior, conservar el tag
      const tagEl = detailImageBox.querySelector('.product-tag');
      detailImageBox.innerHTML = '';
      if (tagEl) detailImageBox.appendChild(tagEl);
      const img = document.createElement('img');
      img.src = p.imagen;
      img.alt = p.nombre;
      img.className = 'product-detail-img-real';
      let intentoFallback = 0;
      img.onerror = function () {
        if (intentoFallback === 0 && p.imagenAlt) {
          intentoFallback = 1;
          img.src = p.imagenAlt;
        } else {
          intentoFallback = 2;
          img.src = generarPlaceholder(p);
          img.onerror = null;
        }
      };
      detailImageBox.appendChild(img);
    }

    // Actualizar campos meta
    const metaItems = document.querySelectorAll('.product-detail-meta strong');
    if (metaItems.length >= 4) {
      metaItems[0].textContent = 'El Cielo Selection';
      metaItems[1].textContent = p.vol;
      metaItems[2].textContent = p.grado;
      metaItems[3].textContent = p.origen;
    }

    // Actualizar botón de favorito del detalle
    const favText = document.getElementById('detail-fav-text');
    const favBtn  = document.getElementById('detail-fav-btn');
    const esFavorito = favoritos.includes(p.id);
    if (favText) favText.textContent = esFavorito ? 'Quitar de favoritos' : 'Agregar a favoritos';
    if (favBtn)  favBtn.classList.toggle('active', esFavorito);
    if (favBtn)  favBtn.setAttribute('data-fav-id', p.id);

    navigateTo('detalle-producto');
  }

  // Botón de favorito dentro del detalle
  const detailFavBtn = document.getElementById('detail-fav-btn');
  if (detailFavBtn) {
    detailFavBtn.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      if (!productoActual) return;
      toggleFavorito(productoActual.id);
      // Refrescar texto del botón
      const esFav = favoritos.includes(productoActual.id);
      document.getElementById('detail-fav-text').textContent = esFav ? 'Quitar de favoritos' : 'Agregar a favoritos';
      detailFavBtn.classList.toggle('active', esFav);
    });
  }

  // Botón "Agregar al carrito" del detalle
  document.querySelectorAll('.btn-add-detail').forEach((btn) => {
    btn.addEventListener('click', () => {
      const original = btn.innerHTML;
      btn.innerHTML = '✓ Agregado al carrito';
      btn.style.background = '#10B981';
      setTimeout(() => {
        btn.innerHTML = original;
        btn.style.background = '';
      }, 1200);
      incrementarContadorCarrito();
      if (productoActual) agregarNotificacion('Producto agregado', `${productoActual.nombre} se añadió al carrito.`);
    });
  });


  // =========================================================
  // 13. RESEÑAS - calificación con estrellas
  // =========================================================
  const starRating = document.getElementById('star-rating');
  let currentRating = 0;
  if (starRating) {
    const stars = starRating.querySelectorAll('span');
    stars.forEach((star) => {
      star.addEventListener('click', () => {
        currentRating = parseInt(star.getAttribute('data-star'), 10);
        stars.forEach((s, i) => {
          const filled = i < currentRating;
          s.classList.toggle('filled', filled);
          s.textContent = filled ? '★' : '☆';
        });
      });
    });
  }

  // Envío del formulario de reseña
  const reviewForm = document.getElementById('review-form');
  if (reviewForm) {
    reviewForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const texto = document.getElementById('review-text').value.trim();
      if (currentRating === 0 || !texto) {
        showModal('modal-campos');
        return;
      }
      // Insertar reseña al inicio de la lista
      const list = document.getElementById('reviews-list');
      if (list) {
        const reviewCard = document.createElement('article');
        reviewCard.className = 'review-card';
        reviewCard.innerHTML = `
          <div class="review-head">
            <div class="review-avatar">YO</div>
            <div>
              <strong>Tú</strong>
              <span class="stars">${'★'.repeat(currentRating)}${'☆'.repeat(5 - currentRating)}</span>
            </div>
            <span class="review-date">Ahora</span>
          </div>
          <p>${texto.replace(/</g, '&lt;')}</p>
        `;
        list.insertBefore(reviewCard, list.firstChild);
      }
      reviewForm.reset();
      currentRating = 0;
      starRating.querySelectorAll('span').forEach((s) => {
        s.classList.remove('filled');
        s.textContent = '☆';
      });
      agregarNotificacion('Reseña publicada', 'Gracias por compartir tu opinión.');
    });
  }


  // =========================================================
  // 14. PERFIL - guardar cambios
  // =========================================================
  const profileForm = document.getElementById('profile-form');
  if (profileForm) {
    profileForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const nombres   = document.getElementById('profile-nombres').value.trim();
      const apellidos = document.getElementById('profile-apellidos').value.trim();
      const email     = document.getElementById('profile-email').value.trim();
      if (!nombres || !apellidos || !email) {
        showModal('modal-campos');
        return;
      }
      // Actualizar tarjeta de avatar
      const dispName  = document.getElementById('profile-display-name');
      const dispEmail = document.getElementById('profile-display-email');
      if (dispName)  dispName.textContent  = `${nombres} ${apellidos}`;
      if (dispEmail) dispEmail.textContent = email;
      agregarNotificacion('Perfil actualizado', 'Tus datos personales se guardaron correctamente.');
      alert('✓ Cambios guardados correctamente');
    });
  }

  const passwordForm = document.getElementById('password-form');
  if (passwordForm) {
    passwordForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const actual    = document.getElementById('password-actual').value.trim();
      const nueva     = document.getElementById('password-nueva').value.trim();
      const confirmar = document.getElementById('password-confirmar').value.trim();
      if (!actual || !nueva || !confirmar) {
        showModal('modal-campos');
        return;
      }
      if (nueva !== confirmar) {
        alert('⚠  Las contraseñas nuevas no coinciden');
        return;
      }
      if (nueva.length < 6) {
        alert('⚠  La contraseña debe tener al menos 6 caracteres');
        return;
      }
      passwordForm.reset();
      agregarNotificacion('Contraseña cambiada', 'Tu contraseña se actualizó correctamente.');
      alert('✓ Contraseña actualizada');
    });
  }


  // =========================================================
  // 15. NOTIFICACIONES
  // =========================================================
  const notificaciones = [
    { titulo: '¡Bienvenido!',    texto: 'Explora nuestro catálogo y aprovecha las promociones activas.', tiempo: 'Ahora',      leida: false },
    { titulo: 'Promo 2x1',       texto: 'En vinos tintos solo este fin de semana.',                     tiempo: 'Hace 2 horas', leida: false },
    { titulo: 'Pedido enviado',  texto: 'Tu pedido #1234 está en camino.',                              tiempo: 'Hace 1 día',   leida: true  },
  ];

  function agregarNotificacion(titulo, texto) {
    notificaciones.unshift({ titulo, texto, tiempo: 'Ahora', leida: false });
    renderizarNotificaciones();
  }

  function renderizarNotificaciones() {
    const list = document.getElementById('notif-list');
    const dot  = document.getElementById('notif-dot');
    if (!list) return;

    const sinLeer = notificaciones.filter((n) => !n.leida).length;
    if (dot) dot.classList.toggle('active', sinLeer > 0);

    if (notificaciones.length === 0) {
      list.innerHTML = '<li class="notif-empty">No tienes notificaciones</li>';
      return;
    }

    list.innerHTML = notificaciones.map((n) => `
      <li class="notif-item ${n.leida ? '' : 'unread'}">
        <p class="notif-item-title">${n.titulo}</p>
        <p class="notif-item-text">${n.texto}</p>
        <p class="notif-item-time">${n.tiempo}</p>
      </li>
    `).join('');
  }

  // Toggle del dropdown de notificaciones
  const notifBtn      = document.getElementById('notif-btn');
  const notifDropdown = document.getElementById('notif-dropdown');
  if (notifBtn && notifDropdown) {
    notifBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      notifDropdown.classList.toggle('active');
      const isActive = notifDropdown.classList.contains('active');
      notifDropdown.setAttribute('aria-hidden', !isActive);
    });
    // Cerrar al hacer click fuera
    document.addEventListener('click', (e) => {
      if (!e.target.closest('.notif-wrap')) {
        notifDropdown.classList.remove('active');
        notifDropdown.setAttribute('aria-hidden', 'true');
      }
    });
  }

  // Marcar todas como leídas
  const notifClear = document.getElementById('notif-clear');
  if (notifClear) {
    notifClear.addEventListener('click', () => {
      notificaciones.forEach((n) => (n.leida = true));
      renderizarNotificaciones();
    });
  }

  // =========================================================
  // 17. MÓDULOS DEL ADMINISTRADOR
  // =========================================================

  // ---- Datos simulados (clientes registrados) ----
  const clientesAdmin = [
    { id: 1, nombre: 'María Rodríguez',   correo: 'maria.r@email.com',     cedula: '1234567890', pedidos: 12, total: 4280000, estado: 'activo' },
    { id: 2, nombre: 'Juan Carlos Pérez', correo: 'juancarlos@email.com',  cedula: '1098765432', pedidos: 8,  total: 2150000, estado: 'activo' },
    { id: 3, nombre: 'Ana Gómez',         correo: 'ana.gomez@email.com',   cedula: '1122334455', pedidos: 25, total: 8920000, estado: 'suspendido' },
  ];

  // ---- Datos simulados (promociones admin) ----
  // Estas son las MISMAS promociones que ve el cliente en la pestaña "Promos"
  const promocionesAdmin = [
    { id: 1, nombre: 'Whisky Premium 12 años -30%', producto: 'Whisky Premium',     tipo: 'descuento', valor: 30, inicio: '2026-04-20', fin: '2026-05-15', activa: true },
    { id: 2, nombre: '2x1 en Vinos Tintos',          producto: 'Vino Tinto Reserva', tipo: '2x1',       valor: 50, inicio: '2026-05-01', fin: '2026-05-08', activa: true },
    { id: 3, nombre: 'Vodka Importado -20%',         producto: 'Vodka Importado',    tipo: 'descuento', valor: 20, inicio: '2026-04-25', fin: '2026-05-30', activa: true },
    { id: 4, nombre: 'Combo Tequila + Limones -15%', producto: 'Tequila Reposado',   tipo: 'combo',     valor: 15, inicio: '2026-05-01', fin: '2026-06-01', activa: true },
  ];


  // ---- Renderizar tabla de USUARIOS ----
  function renderizarUsuarios(filtroTexto = '', filtroEstado = 'todos') {
    const tbody = document.getElementById('users-tbody');
    if (!tbody) return;

    const filtrados = clientesAdmin.filter((c) => {
      const coincideTexto = !filtroTexto ||
        c.nombre.toLowerCase().includes(filtroTexto.toLowerCase()) ||
        c.correo.toLowerCase().includes(filtroTexto.toLowerCase()) ||
        c.cedula.includes(filtroTexto);
      const coincideEstado = filtroEstado === 'todos' || c.estado === filtroEstado;
      return coincideTexto && coincideEstado;
    });

    if (filtrados.length === 0) {
      tbody.innerHTML = '<tr><td colspan="7" style="text-align:center; padding:30px; color:var(--gris-tenue);">No se encontraron usuarios</td></tr>';
      return;
    }

    tbody.innerHTML = filtrados.map((c) => `
      <tr data-user-id="${c.id}">
        <td><strong>${c.nombre}</strong></td>
        <td>${c.correo}</td>
        <td>${c.cedula}</td>
        <td>${c.pedidos}</td>
        <td>${formatearCOP(c.total)}</td>
        <td><span class="status-badge status-${c.estado === 'activo' ? 'active' : 'suspended'}">${c.estado === 'activo' ? 'Activo' : 'Suspendido'}</span></td>
        <td>
          <div class="table-actions">
            <button class="btn-icon-sm" data-action="ver" data-id="${c.id}">Ver</button>
            <button class="btn-icon-sm" data-action="toggle" data-id="${c.id}">${c.estado === 'activo' ? 'Suspender' : 'Reactivar'}</button>
          </div>
        </td>
      </tr>
    `).join('');
  }

  const usersSearch = document.getElementById('users-search');
  const usersFilter = document.getElementById('users-filter');
  if (usersSearch) usersSearch.addEventListener('input', () => renderizarUsuarios(usersSearch.value, usersFilter?.value || 'todos'));
  if (usersFilter) usersFilter.addEventListener('change', () => renderizarUsuarios(usersSearch?.value || '', usersFilter.value));

  // Acciones de la tabla de usuarios
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('#users-tbody [data-action]');
    if (!btn) return;
    const accion = btn.getAttribute('data-action');
    const id = parseInt(btn.getAttribute('data-id'), 10);
    const cliente = clientesAdmin.find((c) => c.id === id);
    if (!cliente) return;

    if (accion === 'ver') {
      alert(`Cliente: ${cliente.nombre}\nCorreo: ${cliente.correo}\nCédula: ${cliente.cedula}\nPedidos: ${cliente.pedidos}\nTotal comprado: ${formatearCOP(cliente.total)}\nEstado: ${cliente.estado}`);
    } else if (accion === 'toggle') {
      cliente.estado = cliente.estado === 'activo' ? 'suspendido' : 'activo';
      renderizarUsuarios(usersSearch?.value || '', usersFilter?.value || 'todos');
      agregarNotificacion('Usuario actualizado', `${cliente.nombre} ahora está ${cliente.estado}.`);
    }
  });


  // ---- Renderizar lista de PROMOCIONES (admin) ----
  function renderizarPromocionesAdmin() {
    const list = document.getElementById('promo-admin-list');
    const count = document.getElementById('promo-count');
    if (!list) return;
    const activas = promocionesAdmin.filter((p) => p.activa);
    if (count) count.textContent = `${activas.length} activas`;
    list.innerHTML = promocionesAdmin.map((p) => `
      <li class="promo-admin-item">
        <div>
          <strong>${p.nombre}</strong>
          <span>${p.producto} · ${p.tipo === 'descuento' ? p.valor + '% off' : p.tipo} · hasta ${p.fin}</span>
        </div>
        <div class="promo-admin-actions">
          <button class="btn-icon-sm" data-promo-action="editar" data-id="${p.id}">Editar</button>
          <button class="btn-icon-sm danger" data-promo-action="eliminar" data-id="${p.id}">Eliminar</button>
        </div>
      </li>
    `).join('');
  }

  // Submit del formulario de nueva promoción
  const promoForm = document.getElementById('promo-form');
  if (promoForm) {
    promoForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const nombre = document.getElementById('promo-nombre').value.trim();
      const productoSelect = document.getElementById('promo-producto');
      const productoNombre = productoSelect.options[productoSelect.selectedIndex]?.text || '';
      const tipo   = document.getElementById('promo-tipo').value;
      const valor  = parseInt(document.getElementById('promo-valor').value, 10) || 0;
      const inicio = document.getElementById('promo-inicio').value;
      const fin    = document.getElementById('promo-fin').value;

      if (!nombre || !productoSelect.value || !inicio || !fin) {
        showModal('modal-campos');
        return;
      }
      promocionesAdmin.unshift({
        id: Date.now(), nombre, producto: productoNombre, tipo, valor, inicio, fin, activa: true
      });
      renderizarPromocionesAdmin();
      promoForm.reset();
      agregarNotificacion('Promoción creada', `"${nombre}" está ahora activa.`);
    });
  }

  // Acciones sobre promociones
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('[data-promo-action]');
    if (!btn) return;
    const accion = btn.getAttribute('data-promo-action');
    const id = parseInt(btn.getAttribute('data-id'), 10);
    const promo = promocionesAdmin.find((p) => p.id === id);
    if (!promo) return;
    if (accion === 'eliminar') {
      if (!confirm(`¿Eliminar la promoción "${promo.nombre}"?`)) return;
      const idx = promocionesAdmin.indexOf(promo);
      promocionesAdmin.splice(idx, 1);
      renderizarPromocionesAdmin();
      agregarNotificacion('Promoción eliminada', `"${promo.nombre}" ya no está activa.`);
    } else if (accion === 'editar') {
      const nuevoValor = prompt(`Editar % de descuento de "${promo.nombre}":`, promo.valor);
      if (nuevoValor !== null) {
        promo.valor = parseInt(nuevoValor, 10) || promo.valor;
        renderizarPromocionesAdmin();
      }
    }
  });


  // ---- REPORTES (con simulación de ventas diarias) ----
  function generarVentasDiariasSimuladas(diasAtras = 30) {
    // Genera un array de objetos { fecha, ventas, pedidos } simulados
    const ventas = [];
    const hoy = new Date();
    for (let i = diasAtras - 1; i >= 0; i--) {
      const fecha = new Date(hoy);
      fecha.setDate(hoy.getDate() - i);
      // Ventas entre 1.5M y 4M COP, mayor en fines de semana
      const esFinde = fecha.getDay() === 0 || fecha.getDay() === 6;
      const base = esFinde ? 2800000 : 2000000;
      const variacion = Math.random() * 1200000 - 600000;
      ventas.push({
        fecha: fecha.toISOString().slice(0, 10),
        ventas: Math.round(base + variacion),
        pedidos: Math.round(20 + Math.random() * 30 + (esFinde ? 10 : 0)),
      });
    }
    return ventas;
  }

  function generarReporte(formato) {
    const tipo   = document.getElementById('report-tipo')?.value || 'ventas';
    const inicio = document.getElementById('report-inicio')?.value;
    const fin    = document.getElementById('report-fin')?.value;
    if (!inicio || !fin) {
      alert('⚠  Por favor selecciona un rango de fechas');
      return;
    }

    // Calcular cuántos días abarca el rango
    const diasRango = Math.max(1, Math.round((new Date(fin) - new Date(inicio)) / 86400000) + 1);
    const datos = generarVentasDiariasSimuladas(diasRango);
    const totalVentas = datos.reduce((s, d) => s + d.ventas, 0);
    const totalPedidos = datos.reduce((s, d) => s + d.pedidos, 0);
    const promedioVentas = Math.round(totalVentas / datos.length);
    const mejorDia = datos.reduce((mejor, d) => d.ventas > mejor.ventas ? d : mejor, datos[0]);

    // Mostrar resumen del reporte simulado
    const resumen =
      `— Reporte de ${tipo.toUpperCase()} generado\n\n` +
      `Formato: ${formato.toUpperCase()}\n` +
      `Período: ${inicio} — ${fin} (${datos.length} días)\n` +
      `─────────────────────────────────\n` +
      `Total ventas:        ${formatearCOP(totalVentas)}\n` +
      `Total pedidos:       ${totalPedidos}\n` +
      `Promedio diario:     ${formatearCOP(promedioVentas)}\n` +
      `Mejor día:           ${mejorDia.fecha} (${formatearCOP(mejorDia.ventas)})\n\n` +
      `(En producción se descargaría el archivo)`;

    alert(resumen);

    // Mostrar el detalle también en la lista de reportes generados
    const list = document.getElementById('reports-list');
    if (list) {
      const ahora = new Date().toLocaleDateString('es-CO');
      const li = document.createElement('li');
      li.innerHTML = `
        <span>Reporte de ${tipo} — ${inicio} a ${fin}</span>
        <span class="report-meta">${formato.toUpperCase()} · ${datos.length} días · ${ahora}</span>
        <button class="btn-link">Descargar</button>
      `;
      list.insertBefore(li, list.firstChild);
    }

    agregarNotificacion('Reporte generado', `${tipo} (${formato.toUpperCase()}) — ${formatearCOP(totalVentas)} en ${datos.length} días.`);
  }

  document.getElementById('btn-report-pdf')?.addEventListener('click', () => generarReporte('pdf'));
  document.getElementById('btn-report-excel')?.addEventListener('click', () => generarReporte('excel'));
  document.getElementById('btn-report-csv')?.addEventListener('click', () => generarReporte('csv'));


  // ---- Inicializar TODOS los módulos admin ----
  renderizarUsuarios();
  renderizarPromocionesAdmin();


  // ---- DASHBOARD: simular ventas diarias dinámicas ----
  function actualizarDashboard() {
    // Generar 7 días de ventas simuladas
    const datosSemana = generarVentasDiariasSimuladas(7);
    const ventasHoy   = datosSemana[datosSemana.length - 1].ventas;
    const pedidosHoy  = datosSemana[datosSemana.length - 1].pedidos;
    const ventasAyer  = datosSemana[datosSemana.length - 2].ventas;
    const pedidosAyer = datosSemana[datosSemana.length - 2].pedidos;

    // Calcular tendencias
    const tendenciaVentas = ((ventasHoy - ventasAyer) / ventasAyer * 100).toFixed(0);
    const tendenciaPedidos = pedidosHoy - pedidosAyer;

    // Actualizar KPIs
    const kpis = document.querySelectorAll('#dashboard-admin .kpi-value');
    if (kpis.length >= 4) {
      kpis[0].textContent = formatearCOP(ventasHoy);
      kpis[1].textContent = pedidosHoy;
      // kpis[2] (stock bajo) y kpis[3] (clientes) se quedan estáticos
      kpis[3].textContent = clientesAdmin.length + ' (' + clientesAdmin.filter(c=>c.estado==='activo').length + ' activos)';
    }

    const tendencias = document.querySelectorAll('#dashboard-admin .kpi-trend');
    if (tendencias.length >= 2) {
      const t1 = tendenciaVentas >= 0 ? `↑ ${tendenciaVentas}% vs ayer` : `↓ ${Math.abs(tendenciaVentas)}% vs ayer`;
      tendencias[0].textContent = t1;
      tendencias[0].className = 'kpi-trend ' + (tendenciaVentas >= 0 ? 'up' : 'down');
      const t2 = tendenciaPedidos >= 0 ? `↑ ${tendenciaPedidos} vs ayer` : `↓ ${Math.abs(tendenciaPedidos)} vs ayer`;
      tendencias[1].textContent = t2;
      tendencias[1].className = 'kpi-trend ' + (tendenciaPedidos >= 0 ? 'up' : 'down');
    }

    // Actualizar gráfico de barras
    const barChart = document.getElementById('bar-chart');
    if (barChart) {
      const max = Math.max(...datosSemana.map(d => d.ventas));
      const dias = ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'];
      // Reordenar para empezar en lunes
      const hoyIdx = new Date().getDay();
      const ordenDias = [];
      for (let i = 6; i >= 0; i--) {
        const d = new Date();
        d.setDate(d.getDate() - i);
        ordenDias.push(dias[(d.getDay() + 6) % 7]); // L,M,X,J,V,S,D
      }
      barChart.innerHTML = datosSemana.map((d, i) => {
        const pct = Math.round((d.ventas / max) * 100);
        const ventasM = (d.ventas / 1000000).toFixed(1);
        return `
          <div class="bar-row">
            <span class="bar-label">${ordenDias[i]}</span>
            <div class="bar-track"><div class="bar-fill" style="width:${pct}%"></div></div>
            <span class="bar-value">$ ${ventasM}M</span>
          </div>
        `;
      }).join('');
    }
  }

  actualizarDashboard();


  // ---- INVENTARIO: Agregar nuevo producto ----
  const btnAddProduct = document.getElementById('btn-add-product');
  if (btnAddProduct) {
    btnAddProduct.addEventListener('click', () => showModal('modal-add-product'));
  }

  const addProductForm = document.getElementById('add-product-form');
  if (addProductForm) {
    addProductForm.addEventListener('submit', (e) => {
      e.preventDefault();
      const nombre    = document.getElementById('new-prod-nombre').value.trim();
      const categoria = document.getElementById('new-prod-categoria').value;
      const stock     = parseInt(document.getElementById('new-prod-stock').value, 10);
      const precio    = parseInt(document.getElementById('new-prod-precio').value, 10);

      if (!nombre || !categoria || isNaN(stock) || isNaN(precio)) {
        showModal('modal-campos');
        return;
      }

      // Insertar nueva fila al inicio de la tabla del inventario
      const tbody = document.querySelector('#inventario .inventory-table tbody');
      if (tbody) {
        // Determinar badge de stock
        let stockHtml, badgeHtml;
        if (stock === 0) {
          stockHtml = `<span class="stock-danger">0 unidades</span>`;
          badgeHtml = `<span class="badge badge-danger"><svg viewBox="0 0 24 24" style="stroke:currentColor; fill:none; stroke-width:2"><circle cx="12" cy="12" r="10" stroke-linecap="round"/><line x1="15" y1="9" x2="9" y2="15" stroke-linecap="round"/><line x1="9" y1="9" x2="15" y2="15" stroke-linecap="round"/></svg>Agotado</span>`;
        } else if (stock < 10) {
          stockHtml = `<span class="stock-warning">${stock} unidades</span>`;
          badgeHtml = `<span class="badge badge-warning"><svg viewBox="0 0 24 24" style="stroke:currentColor; fill:none; stroke-width:2"><circle cx="12" cy="12" r="10" stroke-linecap="round"/><line x1="12" y1="8" x2="12" y2="12" stroke-linecap="round"/><line x1="12" y1="16" x2="12.01" y2="16" stroke-linecap="round"/></svg>Stock Bajo</span>`;
        } else {
          stockHtml = `${stock} unidades`;
          badgeHtml = `<span class="badge badge-success"><svg viewBox="0 0 24 24" style="stroke:currentColor; fill:none; stroke-width:2"><path d="M22 11.08V12a10 10 0 11-5.93-9.14" stroke-linecap="round" stroke-linejoin="round"/><polyline points="22 4 12 14.01 9 11.01" stroke-linecap="round" stroke-linejoin="round"/></svg>Disponible</span>`;
        }

        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td><div class="product-cell"><div class="product-icon-sm"><svg viewBox="0 0 24 24"><path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z" stroke-linecap="round" stroke-linejoin="round"/></svg></div>${nombre}</div></td>
          <td>${categoria}</td>
          <td>${stockHtml}</td>
          <td>${badgeHtml}</td>
          <td class="price-cell">${formatearCOP(precio)}</td>
          <td class="actions-cell"><button class="btn-edit"><svg viewBox="0 0 24 24"><path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" stroke-linecap="round" stroke-linejoin="round"/><path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke-linecap="round" stroke-linejoin="round"/></svg>Editar</button></td>
        `;
        // Animación: fila aparece desde arriba
        tr.style.background = 'rgba(245, 158, 11, 0.15)';
        tbody.insertBefore(tr, tbody.firstChild);
        setTimeout(() => { tr.style.transition = 'background 1s'; tr.style.background = ''; }, 100);
      }

      // Cerrar modal y resetear
      hideModal(document.getElementById('modal-add-product'));
      addProductForm.reset();

      agregarNotificacion('Producto agregado', `"${nombre}" se añadió al inventario.`);
    });
  }


  // Inicializar
  renderizarNotificaciones();
  actualizarVistaFavoritos();


  // =========================================================
  // 16. SIDEBAR M—VIL (botón hamburguesa)
  // =========================================================
  const sidebarToggle  = document.getElementById('sidebar-toggle');
  const sidebarOverlay = document.getElementById('sidebar-overlay');
  const sidebar        = document.querySelector('.dashboard-nav');

  function abrirSidebar() {
    if (sidebar) sidebar.classList.add('open');
  }
  function cerrarSidebar() {
    if (sidebar) sidebar.classList.remove('open');
  }

  if (sidebarToggle) sidebarToggle.addEventListener('click', abrirSidebar);
  if (sidebarOverlay) sidebarOverlay.addEventListener('click', cerrarSidebar);

  // Al hacer click en una pestaña, cerrar sidebar (en móvil)
  document.querySelectorAll('.dashboard-tab, .dashboard-logout').forEach((btn) => {
    btn.addEventListener('click', () => {
      // Solo cerrar si está realmente abierto (móvil)
      if (sidebar && sidebar.classList.contains('open')) {
        cerrarSidebar();
      }
    });
  });


  console.log('Licorería El Cielo · Sistema cargado correctamente');
  console.log('Cliente:        cliente@elcielo.com / 123456');
  console.log('Administrador:  admin@elcielo.com   / admin123');
});
