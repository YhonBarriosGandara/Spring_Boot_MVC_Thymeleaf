-- ==========================================================
-- Sistema AgroFincas - Spring Boot MVC + Thymeleaf
-- Script DML: Datos iniciales de prueba
-- ==========================================================

-- Usuarios iniciales
INSERT INTO usuario (id, clave, nombre, rol, email) VALUES
('admin', 'admin123', 'Administrador Principal', 'ADMINISTRADOR', 'ybarriosg@unicartagena.edu.co'),
('jrodriguez', 'clave2026', 'Juan Rodriguez', 'OPERADOR', 'jrodriguez@agroempresa.co'),
('mgarcia', 'segura789', 'Maria Garcia', 'OPERADOR', 'mgarcia@campocolombia.com'),
('cramirez', 'finca456', 'Carlos Ramirez', 'CONSULTOR', 'cramirez@agrovida.org')
ON CONFLICT (id) DO NOTHING;

-- Fincas iniciales
INSERT INTO finca (nombre, num_hectareas, metros_cuadrados, propietario, capataz, pais, departamento, ciudad, produce_leche, produce_cereales, produce_frutas, produce_verduras) VALUES
('Finca La Esperanza', 120.50, 1205000.00, 'Roberto Gómez', 'Pedro Martínez', 'Colombia', 'Antioquia', 'Rionegro', true, false, true, false),
('Hacienda La Primavera', 350.00, 3500000.00, 'Elena Vargas', 'Luis Morales', 'Colombia', 'Cundinamarca', 'Facatativá', true, true, false, false),
('Villa Paraíso', 45.00, 450000.00, 'Carlos Mendoza', 'Jorge Ruiz', 'Colombia', 'Bolívar', 'Turbaco', false, false, true, true),
('Granja San Jerónimo', 85.00, 850000.00, 'Ana Lucía Torres', 'Andrés Castro', 'Colombia', 'Boyacá', 'Duitama', false, true, false, true),
('Agropecuaria Santa Lucía', 500.00, 5000000.00, 'Fernando Ospina', 'Héctor Jiménez', 'Colombia', 'Meta', 'Villavicencio', true, true, false, false),
('Finca El Encanto', 60.00, 600000.00, 'Patricia Herrera', 'Nelson Ortiz', 'Colombia', 'Santander', 'San Gil', false, false, true, true),
('Hato Ganadero Los Pinos', 220.00, 2200000.00, 'Guillermo Restrepo', 'Manuel Benítez', 'Colombia', 'Córdoba', 'Montería', true, false, false, false);
