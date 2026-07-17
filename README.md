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
├── .vscode/                        → Configuración del proyecto para VS Code
└── README.md
```

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
