# Licorería El Cielo - Módulo de Productos (Java + JDBC)

Módulo de gestión del inventario de productos de la Licorería El Cielo, desarrollado en Java con conexión a base de datos MySQL por medio de JDBC.

**Evidencia:** GA7-220501096-AA2-EV01. Codificación de módulos del software.
**Programa:** Análisis y Desarrollo de Software (ADSO) - SENA.

## Estructura del proyecto

```
licoreria-el-cielo-java/
├── database/
│   └── licoreria_el_cielo.sql      → Script de creación de la base de datos
├── lib/
│   └── (mysql-connector-j.jar)     → Driver JDBC de MySQL
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
└── README.md
```

## Estándares de codificación aplicados

- **Paquetes:** minúsculas (`com.licoreriaelcielo.modelo`).
- **Clases:** PascalCase (`Producto`, `ProductoDao`, `ConexionBaseDatos`).
- **Métodos:** camelCase iniciando con verbo (`listarProductos`, `agregarProducto`).
- **Variables:** camelCase descriptivo (`nombreProducto`, `filasAfectadas`).
- **Constantes:** MAYÚSCULAS con guion bajo (`OPCION_SALIR`, `URL`).
- Encapsulamiento con atributos `private` y métodos get/set.
- Sentencias preparadas (`PreparedStatement`) para evitar inyección SQL.

## Requisitos

1. **JDK 17 o superior** (probado con JDK 21).
2. **MySQL Server** (se puede usar XAMPP con phpMyAdmin).
3. **Driver JDBC de MySQL** (MySQL Connector/J): descargarlo desde
   https://dev.mysql.com/downloads/connector/j/ y guardar el archivo `.jar`
   en la carpeta `lib/`.

## Pasos para ejecutar

1. **Crear la base de datos:** abrir MySQL (o phpMyAdmin) y ejecutar el script
   `database/licoreria_el_cielo.sql`. Esto crea la base de datos, la tabla
   `productos` y carga los 6 productos iniciales del catálogo.

2. **Configurar la conexión:** si el usuario o la contraseña de MySQL son
   diferentes, cambiarlos en la clase
   `src/com/licoreriaelcielo/conexion/ConexionBaseDatos.java`
   (constantes `USUARIO` y `CONTRASENA`).

3. **Compilar** (desde la raíz del proyecto):

   ```
   javac -d bin src/com/licoreriaelcielo/conexion/*.java src/com/licoreriaelcielo/modelo/*.java src/com/licoreriaelcielo/dao/*.java src/com/licoreriaelcielo/vista/*.java
   ```

4. **Ejecutar** (Windows usa `;` como separador, Linux/Mac usa `:`):

   ```
   java -cp "bin;lib/*" com.licoreriaelcielo.vista.MenuProductos
   ```

   En NetBeans o IntelliJ basta con agregar el `.jar` del conector a las
   librerías del proyecto y ejecutar la clase `MenuProductos`.

## Funcionalidades del módulo

1. Listar todos los productos del inventario.
2. Buscar un producto por su ID.
3. Agregar un nuevo producto.
4. Actualizar los datos de un producto.
5. Eliminar un producto (con confirmación).

Todas las operaciones se realizan directamente sobre la base de datos
MySQL mediante JDBC.
