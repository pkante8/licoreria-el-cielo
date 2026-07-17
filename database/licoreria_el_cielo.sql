-- ============================================================
-- LICORERÍA EL CIELO
-- Script de creación de la base de datos
-- Evidencia GA7-220501096-AA2-EV01
-- Motor: MySQL
-- ============================================================

-- 1. Crear la base de datos
CREATE DATABASE IF NOT EXISTS licoreria_el_cielo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE licoreria_el_cielo;

-- 2. Tabla de productos (módulo de inventario)
CREATE TABLE IF NOT EXISTS productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    categoria VARCHAR(50) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    volumen VARCHAR(20),
    grado_alcohol VARCHAR(10),
    origen VARCHAR(50)
);

-- 3. Datos iniciales (los mismos productos del catálogo web)
INSERT INTO productos (nombre, descripcion, categoria, precio, stock, volumen, grado_alcohol, origen) VALUES
('Whisky Premium',     'Whisky escocés añejado 12 años, notas ahumadas y especiadas',   'Whisky',    360000, 15, '750 ml', '40%',   'Escocia'),
('Vino Tinto Reserva', 'Vino tinto de cuerpo completo con toques de cereza y vainilla', 'Vino',      182000, 25, '750 ml', '13.5%', 'Argentina'),
('Vodka Importado',    'Vodka destilado cinco veces para una pureza excepcional',       'Vodka',     140000, 20, '750 ml', '40%',   'Rusia'),
('Ron Añejo',          'Ron añejado siete años en barricas de roble',                   'Ron',       212000, 18, '750 ml', '40%',   'Cuba'),
('Tequila Reposado',   '100% agave azul, reposado en barricas durante seis meses',      'Tequila',   196000, 12, '750 ml', '38%',   'México'),
('Champagne Brut',     'Champagne francés con burbujas finas y persistentes',           'Champagne', 380000, 10, '750 ml', '12%',   'Francia');

-- 4. Consulta de verificación
SELECT * FROM productos;
