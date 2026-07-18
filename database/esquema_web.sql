-- ============================================================
-- LICORERÍA EL CIELO
-- Tablas adicionales para la aplicación web (pedidos, favoritos, promociones)
-- Motor: MySQL
-- ============================================================

USE licoreria_el_cielo;

-- ----- Pedidos (cabecera) -----
CREATE TABLE IF NOT EXISTS pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'En preparación',
    total DECIMAL(12, 2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ----- Items de cada pedido (detalle) -----
CREATE TABLE IF NOT EXISTS pedido_items (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_producto INT NOT NULL,
    nombre_producto VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_item_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE
);

-- ----- Favoritos (relación usuario - producto) -----
CREATE TABLE IF NOT EXISTS favoritos (
    id_usuario INT NOT NULL,
    id_producto INT NOT NULL,
    PRIMARY KEY (id_usuario, id_producto),
    CONSTRAINT fk_fav_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_fav_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- ----- Promociones -----
CREATE TABLE IF NOT EXISTS promociones (
    id_promocion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    producto VARCHAR(100),
    tipo VARCHAR(20) NOT NULL,          -- descuento / 2x1 / combo
    valor INT NOT NULL DEFAULT 0,       -- % de descuento
    fecha_inicio DATE,
    fecha_fin DATE,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

-- Promociones de ejemplo (las mismas del prototipo)
INSERT INTO promociones (nombre, producto, tipo, valor, fecha_inicio, fecha_fin, activa) VALUES
('Whisky Premium 12 años -30%', 'Whisky Premium',     'descuento', 30, '2026-04-20', '2026-05-15', TRUE),
('2x1 en Vinos Tintos',          'Vino Tinto Reserva', '2x1',       50, '2026-05-01', '2026-05-08', TRUE),
('Vodka Importado -20%',         'Vodka Importado',    'descuento', 20, '2026-04-25', '2026-05-30', TRUE),
('Combo Tequila + Limones -15%', 'Tequila Reposado',   'combo',     15, '2026-05-01', '2026-06-01', TRUE);
