# Plan — Migrar el frontend HTML a Java Web (Servlets + JSP)

Objetivo: convertir la SPA `web/index.html` a una aplicación Java web **ordenada,
módulo por módulo, formulario por formulario**, usando **Servlets + JSP + JDBC**,
reutilizando lo que ya existe (`ConexionBaseDatos`, `Producto`, `ProductoDao`).

> Estado: **PLAN — pendiente de aprobación**. No se escribe código hasta que lo apruebes.

---

## 1. Decisiones de arquitectura

- **Patrón:** MVC por capas.
  - `modelo/` → clases de dominio (POJO): `Usuario`, `Producto`, `Pedido`, `Promocion`, `Favorito`.
  - `dao/` → acceso a datos con JDBC (uno por entidad).
  - `servlet/` → controladores (uno por módulo/formulario).
  - `web/WEB-INF/jsp/` → vistas JSP (una por pantalla).
  - `filtro/` → `AuthFilter` (control de sesión y rol; reemplaza los guardas de ruta del JS).
- **Servidor:** Apache **Tomcat 10.1** (API `jakarta.servlet`, compatible con JDK 21).
- **Build:** convertir a **Maven** (empaquetado `war`). Dependencias:
  `jakarta.servlet-api` (provided), `jakarta.servlet.jsp.jstl` (JSTL), `mysql-connector-j`.
- **Sesión:** `HttpSession` guarda el usuario logueado y su rol → sustituye
  `usuarioActual` / `rolSeleccionado` del JS.
- **Vistas compartidas:** `header.jsp` + `sidebar.jsp` (incluidos con `<jsp:include>`)
  reemplazan la navegación SPA. El `styles.css` se reutiliza tal cual en `web/css/`.
- **Regla de conversión (equivalencias):**
  | HTML/JS actual | Java Web |
  |---|---|
  | `<section id="x">` (pantalla) | `x.jsp` + `XServlet` |
  | `navigateTo()` / hash routing | URLs reales `/x` + `AuthFilter` |
  | `<form>` + validación JS | `<form method=post>` + validación en el Servlet |
  | arrays `productos`, `clientesAdmin`… (datos demo) | tablas MySQL vía DAO |
  | modales de error JS | atributos de request + bloque JSP condicional |
  | `showModal('modal-campos')` | `request.setAttribute("error", ...)` → JSP |

---

## 2. Base de datos (ampliar el esquema)

Hoy solo existe la tabla `productos`. Se añadirán scripts en `database/`:

- [ ] `usuarios` (id, nombres, apellidos, email, password, cedula, telefono, direccion, rol, estado)
- [ ] `pedidos` (id, usuario_id, fecha, estado, total) + `pedido_items` (pedido_id, producto_id, cantidad, precio)
- [ ] `favoritos` (usuario_id, producto_id)
- [ ] `promociones` (id, nombre, producto_id, tipo, valor, fecha_inicio, fecha_fin, activa)
- [ ] Datos semilla (2 usuarios: cliente/admin; las promos de ejemplo).

---

## 3. Mapa de módulos (pantalla HTML → Servlet + JSP)

### Núcleo / autenticación
- [ ] **Login** (`#login`) → `LoginServlet` + `login.jsp` · `UsuarioDao`, `Usuario` · crea sesión + rol
- [ ] **Registro** (`#registro`) → `RegistroServlet` + `registro.jsp`
- [ ] **Logout** → `LogoutServlet` (invalida sesión)
- [ ] **AuthFilter** → protege rutas, redirige por rol (cliente/admin)

### Módulos CLIENTE
- [ ] **Catálogo** (`#catalogo`) → `CatalogoServlet` + `catalogo.jsp` (lista `ProductoDao.listar()`)
- [ ] **Detalle de producto** (`#detalle-producto`) → `DetalleProductoServlet` + `detalle.jsp`
- [ ] **Carrito** (`#carrito`) → `CarritoServlet` + `carrito.jsp` (carrito en `HttpSession`)
- [ ] **Checkout / Datos cliente** (`#datos-cliente`) → `CheckoutServlet` + `checkout.jsp`
- [ ] **Pedidos** (`#pedidos`) → `PedidoServlet` + `pedidos.jsp` · `PedidoDao`
- [ ] **Perfil** (`#perfil`) → `PerfilServlet` + `perfil.jsp`
- [ ] **Favoritos** (`#favoritos`) → `FavoritoServlet` + `favoritos.jsp`
- [ ] **Promociones** (`#promociones`) → `PromocionServlet` + `promociones.jsp`

### Módulos ADMIN
- [ ] **Dashboard** (`#dashboard-admin`) → `DashboardServlet` + `dashboard.jsp` (KPIs desde BD)
- [ ] **Inventario / CRUD productos** (`#inventario`) → `InventarioServlet` + `inventario.jsp` (reusa `ProductoDao`)
- [ ] **Usuarios** (`#usuarios`) → `UsuarioAdminServlet` + `usuarios.jsp`
- [ ] **Promociones admin** (`#admin-promociones`) → `PromocionAdminServlet` + `admin-promociones.jsp`
- [ ] **Reportes** (`#reportes`) → `ReporteServlet` + `reportes.jsp`

---

## 4. Orden de implementación por fases (entregas revisables)

- [x] **Fase 0 — Andamiaje:** Maven (`pom.xml`, war), `src/main/{java,webapp}`, Tomcat 10 embebido (Cargo), `styles.css` en webapp, `AuthFilter`. ✔ build OK.
- [x] **Fase 1 — Autenticación:** `Usuario`, `UsuarioDao`, tabla `usuarios`, Login + Registro + Logout + Inicio. ✔ **Verificado end-to-end** contra MySQL (login cliente/admin, rol, credenciales inválidas).
- [ ] **Fase 2 — Productos (núcleo):** Catálogo + Detalle + Inventario CRUD (reusa `ProductoDao`).
- [ ] **Fase 3 — Compra:** Carrito + Checkout + Pedidos.
- [ ] **Fase 4 — Cliente extra:** Perfil + Favoritos + Promociones.
- [ ] **Fase 5 — Admin extra:** Dashboard + Usuarios + Promos admin + Reportes.
- [ ] **Fase 6 — Verificación final:** correr en Tomcat, probar cada formulario, capturas.

Cada fase = commit propio (autor `pkante8`, sin coautor) + push cuando lo indiques.

---

## 5. Riesgos / cosas a confirmar

- El proyecto pasa de "consola" a "web con Tomcat": hay que **instalar Tomcat 10.1**
  (o usar el plugin de Maven). ¿Lo instalamos cuando lleguemos a la Fase 0?
- Convertir a Maven reorganiza carpetas (`src/main/java`, `src/main/webapp`).
  El módulo de consola actual (`MenuProductos`) se conserva pero queda aparte.
- Las contraseñas hoy están en texto plano en el JS demo; en Java las guardaremos
  en la tabla `usuarios` (¿texto plano para la entrega académica, o con hash simple?).

---

## Revisión
(Se completa al finalizar cada fase: qué se hizo, qué se probó, qué quedó pendiente.)
