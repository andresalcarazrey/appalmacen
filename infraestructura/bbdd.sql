SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS almacen;
USE almacen;

-- Tabla de productos normales
DROP TABLE IF EXISTS Productos;
CREATE TABLE Productos (
    codigo VARCHAR(20) PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tabla de productos perecederos (hereda de Productos)
DROP TABLE IF EXISTS ProductosPerecederos;
CREATE TABLE ProductosPerecederos (
    codigo VARCHAR(20) PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    fecha_caducidad DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Inserción de datos de ejemplo
INSERT INTO Productos (codigo, descripcion, precio, stock) VALUES
('P001', 'Lápiz', 1.50, 100),
('P002', 'Cuaderno', 3.00, 50),
('P003', 'Borrador', 0.75, 200);

INSERT INTO ProductosPerecederos (codigo, descripcion, precio, stock, fecha_caducidad) VALUES
('P004', 'Yogur', 1.20, 30, '2026-12-31'),
('P005', 'Leche', 0.90, 20, '2026-11-30'),
('P006', 'Pan', 1.10, 40, '2026-10-31');

COMMIT;
