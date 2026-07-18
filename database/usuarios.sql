-- ============================================================
-- LICORERÍA EL CIELO
-- Tabla de usuarios (módulo de autenticación y gestión de usuarios)
-- Motor: MySQL
-- ============================================================

USE licoreria_el_cielo;

-- Tabla de usuarios (clientes y administradores)
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80),
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    cedula VARCHAR(20),
    telefono VARCHAR(30),
    direccion VARCHAR(150),
    rol ENUM('cliente', 'admin') NOT NULL DEFAULT 'cliente',
    estado ENUM('activo', 'suspendido') NOT NULL DEFAULT 'activo'
);

-- Usuarios de prueba (mismas credenciales que el prototipo web)
INSERT INTO usuarios (nombres, apellidos, email, password, cedula, telefono, direccion, rol, estado) VALUES
('María',  'Rodríguez', 'cliente@elcielo.com', '123456',  '1234567890', '+57 300 123 4567', 'Calle 123 # 45-67, Bogotá', 'cliente', 'activo'),
('Admin',  'General',   'admin@elcielo.com',   'admin123', '0000000000', '+57 300 000 0000', 'Sede administrativa',       'admin',   'activo');

SELECT * FROM usuarios;
