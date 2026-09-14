-- ==========================================================
-- Sistema AgroFincas - Spring Boot MVC + Thymeleaf
-- Script DDL: Creacion de tablas para PostgreSQL
-- Ejercicio N.º 4: Finca + Entidad Comun Usuario
-- ==========================================================

DROP TABLE IF EXISTS finca CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- Tabla 1: Entidad Comun Usuario
CREATE TABLE usuario (
    id VARCHAR(50) PRIMARY KEY,
    clave VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Tabla 2: Entidad de Negocio Finca (12 atributos)
CREATE TABLE finca (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    num_hectareas NUMERIC(10, 2) NOT NULL,
    metros_cuadrados NUMERIC(12, 2) NOT NULL,
    propietario VARCHAR(100) NOT NULL,
    capataz VARCHAR(100) NOT NULL,
    pais VARCHAR(50) NOT NULL,
    departamento VARCHAR(50) NOT NULL,
    ciudad VARCHAR(50) NOT NULL,
    produce_leche BOOLEAN NOT NULL DEFAULT FALSE,
    produce_cereales BOOLEAN NOT NULL DEFAULT FALSE,
    produce_frutas BOOLEAN NOT NULL DEFAULT FALSE,
    produce_verduras BOOLEAN NOT NULL DEFAULT FALSE
);
