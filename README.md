# Licorería El Cielo

Sistema de gestión para la Licorería El Cielo, desarrollado en **Java** con conexión a base de datos **MySQL** mediante **JDBC**. El proyecto sigue una arquitectura por capas (modelo, DAO, vista) y aplica buenas prácticas de programación orientada a objetos.

Actualmente incluye el **módulo de gestión de productos** (inventario), pensado para crecer con nuevos módulos (ventas, clientes, reportes, etc.).

## Estructura del proyecto

```
licoreria-el-cielo-java/
├── database/
│   └── licoreria_el_cielo.sql      → Script de creación de la base de datos
├── lib/
│   └── mysql-connector-j-9.7.0.jar → Driver JDBC de MySQL (MySQL Connector/J)
├── src/
│   └── com/licoreriaelcielo/
│       ├── conexion/
│       │   └── ConexionBaseDatos.java   → Conexión JDBC a MySQL
│       ├── modelo/
│       │   └── Producto.java            → Clase del producto (POO)
│       ├── dao/
│       │   └── ProductoDao.java         → Operaciones CRUD con JDBC
│       └── vista/
│           └── MenuProductos.java       → Menú principal por consola
├── web/                            → Prototipo de interfaz web (frontend)
│   ├── index.html                  → SPA: login, catálogo, carrito, admin…
│   ├── styles.css                  → Estilos
│   └── script.js                   → Lógica del cliente (navegación, validaciones)
├── .vscode/                        → Configuración del proyecto para VS Code
└── README.md
```

## Aplicación web en Java (Servlets + JSP + JDBC)

Además del módulo de consola, el proyecto incluye una **aplicación web completa**
construida con **Servlets + JSP + JDBC** sobre **Maven** y **Tomcat 10**, que migra
todo el prototipo `web/` a Java (arquitectura MVC por capas: `modelo`, `dao`,
`servlet`, `filtro` y vistas `WEB-INF/jsp/`).

**Módulos:** login/registro/logout con roles, catálogo, detalle, carrito, checkout,
pedidos, perfil, favoritos, promociones (cliente); dashboard, inventario (CRUD),
usuarios, promociones y reportes (admin).

### Cómo ejecutar la app web
1. Crear la base de datos y tablas (una vez):
   ```
   mysql -u root -p < database/licoreria_el_cielo.sql
   mysql -u root -p < database/usuarios.sql
   mysql -u root -p < database/esquema_web.sql
   ```
2. Levantar el servidor (Tomcat 10 embebido, lo descarga Maven):
   ```
   mvn cargo:run
   ```
3. Abrir **http://localhost:8080/licoreria**
   - Cliente: `cliente@elcielo.com` / `123456`
   - Admin: `admin@elcielo.com` / `admin123`

Requisitos: JDK 17+ y Maven. Ajusta usuario/clave de MySQL en
`src/main/java/com/licoreriaelcielo/conexion/ConexionBaseDatos.java` si difieren.

## Interfaz web (prototipo)

La carpeta `web/` contiene un **prototipo de interfaz** (HTML + CSS + JavaScript)
que muestra cómo se vería el sistema completo: login por rol (cliente/admin),
catálogo, carrito, checkout, favoritos, promociones, panel de administración,
inventario, usuarios y reportes.

> Es una maqueta funcional en el navegador con **datos de ejemplo**; aún no está
> conectada al backend Java/MySQL. Para probarla, abre `web/index.html` en el navegador.
>
> Credenciales de prueba — Cliente: `cliente@elcielo.com` / `123456` ·
> Admin: `admin@elcielo.com` / `admin123`.

## Arquitectura

- **modelo** — clases de dominio (entidades) con encapsulamiento y métodos get/set.
- **dao** — acceso a datos: operaciones CRUD sobre la base de datos vía JDBC.
- **conexion** — gestión centralizada de la conexión a MySQL.
- **vista** — interfaz de usuario por consola.

## Estándares de codificación

- **Paquetes:** minúsculas (`com.licoreriaelcielo.modelo`).
- **Clases:** PascalCase (`Producto`, `ProductoDao`, `ConexionBaseDatos`).
- **Métodos:** camelCase iniciando con verbo (`listarProductos`, `agregarProducto`).
- **Variables:** camelCase descriptivo (`nombreProducto`, `filasAfectadas`).
- **Constantes:** MAYÚSCULAS con guion bajo (`OPCION_SALIR`, `URL`).
- Encapsulamiento con atributos `private` y métodos get/set.
- Sentencias preparadas (`PreparedStatement`) para prevenir inyección SQL.

## Requisitos

1. **JDK 17 o superior** (probado con JDK 21).
2. **MySQL Server** (se puede usar XAMPP con phpMyAdmin).
3. **Driver JDBC de MySQL** (MySQL Connector/J) — ya incluido en `lib/`.

## Configuración

1. **Crear la base de datos:** ejecutar el script `database/licoreria_el_cielo.sql`
   en MySQL (o phpMyAdmin). Crea la base de datos, la tabla `productos` y carga
   el catálogo inicial.

2. **Configurar la conexión:** si el usuario o la contraseña de MySQL son
   distintos, ajustarlos en `src/com/licoreriaelcielo/conexion/ConexionBaseDatos.java`
   (constantes `USUARIO` y `CONTRASENA`).

## Compilación y ejecución

Desde la raíz del proyecto:

```bash
# Compilar
javac -cp "lib/*" -d bin src/com/licoreriaelcielo/**/*.java

# Ejecutar (Windows usa ; como separador; Linux/Mac usa :)
java -cp "bin;lib/*" com.licoreriaelcielo.vista.MenuProductos
```

En NetBeans, IntelliJ o VS Code basta con agregar el `.jar` de `lib/` a las
librerías del proyecto y ejecutar la clase `MenuProductos`.

## Funcionalidades (módulo de productos)

1. Listar todos los productos del inventario.
2. Buscar un producto por su ID.
3. Agregar un nuevo producto.
4. Actualizar los datos de un producto.
5. Eliminar un producto (con confirmación).

Todas las operaciones se realizan sobre la base de datos MySQL mediante JDBC.

## Tecnologías

- Java (JDK 17+)
- MySQL
- JDBC (MySQL Connector/J)
