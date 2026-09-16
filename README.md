# AgroFincas UDC - Sistema de Gestión Agropecuaria

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-green.svg)](https://www.thymeleaf.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)

> **Actividad Académica:** Desarrollo Web - Unidad 2  
> **Tema:** Spring Boot MVC con Thymeleaf: desarrollo web basado en framework  
> **Modalidad:** Individual  
> **Ejercicio Asignado:** N.º 4 — **Finca** (12 atributos de negocio) + Entidad común **Usuario**  
> **Estudiante:** Yhon Barrios Gándara (`ybarriosg@unicartagena.edu.co`)  
> **Institución:** Universidad de Cartagena — Facultad de Ingeniería de Software  

---

## 1. Descripción del Proyecto

**AgroFincas UDC** es una aplicación web empresarial diseñada para la administración, supervisión geográfica y control de producción de predios rurales y fincas agropecuarias.

Siguiendo las directrices pedagógicas y de arquitectura de la **Unidad 2**, la aplicación está construida estrictamente bajo el patrón **Model-View-Controller (MVC) con Server-Side Rendering (SSR)** mediante **Thymeleaf**. 

> **Aclaración Arquitectural Obligatoria:**  
> A diferencia de un backend RESTful que devuelve JSON a un cliente desacoplado (React/Angular/Vue), aquí el servidor Spring Boot procesa cada solicitud HTTP, orquesta la lógica transaccional mediante servicios, consulta la base de datos con Spring Data JPA y genera el código HTML final en el servidor a través de plantillas Thymeleaf, retornando páginas web completas y seguras al navegador.

---

## 2. Arquitectura de Software y Flujo de Petición

La aplicación implementa una arquitectura en capas con estricta separación de responsabilidades e inyección de dependencias (`@Autowired` / Constructor Injection):

```
                        FLUJO DE UNA PETICIÓN EN AGROFINCAS UDC
                        
  [ Navegador Web ]
         │  ▲
 1. HTTP │  │ 7. HTML renderizado
 Request │  │    (Respuesta con estado y datos)
         ▼  │
  ┌───────────────┐        6. Envia Modelo        ┌─────────────────────────┐
  │  @Controller  │ ────────────────────────────> │   Thymeleaf Template    │
  │ (Spring MVC)  │                               │   (resources/templates) │
  └───────────────┘                               └─────────────────────────┘
         │  ▲
 2. Llama│  │ 5. Retorna
 servicio│  │    DTO/Entidad
         ▼  │
  ┌───────────────┐
  │   @Service    │  (Lógica de negocio, reglas de validación y @Transactional)
  └───────────────┘
         │  ▲
 3. Invoca│ │ 4. Retorna
 método  │  │    resultado
         ▼  │
  ┌───────────────┐
  │  @Repository  │  (Spring Data JPA - Queries derivados y JPQL parametrizados)
  └───────────────┘
         │  ▲
         ▼  │  Consultas SQL vía JDBC Driver (HikariCP)
  ┌─────────────────────────────────────────────────────────┐
  │            Base de Datos Relacional PostgreSQL          │
  └─────────────────────────────────────────────────────────┘
```

### Componentes y Responsabilidades:
1. **Entity / Model (`com.udc.fincas.entity`):** Entidades JPA (`Usuario`, `Finca`) mapeadas a tablas relacionales con anotaciones ORM y restricciones de Bean Validation (`@NotBlank`, `@NotNull`, `@Min`, `@Email`).
2. **Repository (`com.udc.fincas.repository`):** Interfaces que extienden `JpaRepository<T, ID>`, encapsulando el acceso a datos sin requerir SQL manual para operaciones estándar y definiendo consultas JPQL para reportes avanzados.
3. **Service (`com.udc.fincas.service`):** Capa transaccional (`@Transactional`) donde residen las reglas de negocio (autocalculo de hectáreas a metros cuadrados, validación de unicidad de identificadores, cifrado/verificación de claves).
4. **Controller (`com.udc.fincas.controller`):** Controladores Spring MVC (`@Controller`) que mapean rutas web (`@GetMapping`, `@PostMapping`), capturan parámetros (`@RequestParam`, `@PathVariable`, `@ModelAttribute`), validan formularios (`@Valid BindingResult`) y retornan nombres lógicos de vistas Thymeleaf.
5. **Thymeleaf Views (`src/main/resources/templates`):** Plantillas HTML semánticas modularizadas con fragmentos reutilizables (`header`, `navbar`, `alerts`, `footer`), manipulación de modelos con expresiones `${...}` y enlace bidireccional de formularios con `th:field`.

---

## 3. Entidades y Atributos de Negocio

### Entidad Finca (Ejercicio N.º 4 — 12 Atributos):
| N.º | Atributo | Tipo de Dato | Descripción / Restricción |
| :---: | :--- | :--- | :--- |
| 1 | `nombre` | `String` | Nombre identificador de la finca (Obligatorio) |
| 2 | `numHectareas` | `BigDecimal` | Extensión en hectáreas (Obligatorio, > 0) |
| 3 | `metrosCuadrados` | `BigDecimal` | Área en m² (Autocalculado: 1 ha = 10,000 m²) |
| 4 | `propietario` | `String` | Nombre completo del dueño de la propiedad |
| 5 | `capataz` | `String` | Nombre del encargado o administrador en campo |
| 6 | `pais` | `String` | País de ubicación (Por defecto: Colombia) |
| 7 | `departamento` | `String` | Departamento / Provincia (Ej: Antioquia, Bolívar) |
| 8 | `ciudad` | `String` | Municipio o ciudad de radicación |
| 9 | `produceLeche` | `boolean` | Indicador de explotación ganadera lechera |
| 10 | `produceCereales` | `boolean` | Indicador de cultivo de cereales (maíz, arroz) |
| 11 | `produceFrutas` | `boolean` | Indicador de producción frutícola |
| 12 | `produceVerduras` | `boolean` | Indicador de producción de hortalizas y legumbres |

### Entidad Usuario (Entidad Común del Sistema):
| Atributo | Tipo | Descripción |
| :--- | :--- | :--- |
| `id` | `String` | Nombre de usuario / Login único en el sistema |
| `clave` | `String` | Contraseña de acceso |
| `nombre` | `String` | Nombre completo del operador o funcionario |
| `rol` | `String` | Rol de permisos: `ADMINISTRADOR`, `OPERADOR`, `CONSULTOR` |
| `email` | `String` | Correo institucional para recuperación de clave |

---

## 4. Funcionalidades Implementadas

* **CRUD Completo de Fincas:** Registro, consulta detallada, edición con validaciones y eliminación protegida por modal.
* **CRUD Completo de Usuarios:** Creación con prevención de duplicados, edición de perfil y eliminación.
* **Autenticación y Control de Sesión:** Manejo de sesiones de usuario con `HttpSession` e interceptor HTTP (`AuthInterceptor`) que protege rutas administrativas contra accesos no autenticados.
* **4 Reportes Parametrizados (2 por entidad):**
  1. *Fincas por Ubicación y Rango de Extensión:* Filtro combinado por departamento y hectáreas mínimas/máximas.
  2. *Fincas por Líneas de Producción:* Filtro multi-criterio que permite consultar predios según combinaciones específicas de rubros agropecuarios (Leche, Cereales, Frutas, Verduras).
  3. *Usuarios por Rol de Acceso:* Consulta selectiva según perfil (`ADMIN`, `OPERADOR`, `CONSULTOR`).
  4. *Usuarios por Coincidencia:* Búsqueda parametrizada en nombres y apellidos.
* **Recuperación de Contraseña por Correo:** Integración con **Jakarta Mail (`spring-boot-starter-mail`)** para generar claves temporales seguras y notificarlas al correo del usuario, con soporte para fallback de desarrollo.

---

## 5. Credenciales de Prueba (Tutoría y Docente)

Para facilitar la evaluación inmediata, la base de datos incluye cuentas precargadas:

| Identificador (Login) | Contraseña | Rol Asignado | Permisos |
| :--- | :--- | :--- | :--- |
| `admin` | `admin123` | `ADMINISTRADOR` | Control total del sistema y gestión de usuarios |
| `jrodriguez` | `clave2026` | `OPERADOR` | Gestión operativa de fincas y reportes |
| `cramirez` | `finca456` | `CONSULTOR` | Consulta y visualización de predios |

---

## 6. Instrucciones de Ejecución

### Opción A: Ejecución con Docker Compose (Recomendada / Más Rápida)

Permite levantar la base de datos PostgreSQL y la aplicación Spring Boot en contenedores aislados con un único comando:

```bash
# 1. Clonar el repositorio
git clone https://github.com/YhonBarriosGandara/Spring_Boot_MVC_Thymeleaf.git
cd Spring_Boot_MVC_Thymeleaf

# 2. Levantar los servicios (PostgreSQL + Spring Boot)
docker compose up -d --build

# 3. Acceder en el navegador
# URL: http://localhost:8080/login
```

Para detener los servicios:
```bash
docker compose down
```

---

### Opción B: Ejecución con Maven Local (App Nativa)

#### Método 1: Base de Datos en Docker + Spring Boot con Maven (Recomendada)
Si no desea instalar ni configurar PostgreSQL manualmente en su sistema operativo, puede iniciar exclusivamente el contenedor de base de datos y ejecutar Spring Boot de forma nativa:

```bash
# 1. Iniciar únicamente el servicio de PostgreSQL en Docker:
docker compose up -d postgres

# 2. Ejecutar la suite de pruebas automatizadas:
mvn clean test

# 3. Iniciar la aplicación Spring Boot nativamente:
mvn spring-boot:run
```

#### Método 2: PostgreSQL Nativo Instalado en el Sistema Operativo

1. **Requisitos Previos:**
   * Java JDK 21 (`java -version`).
   * Apache Maven 3.9+ (`mvn -version`).
   * Servidor PostgreSQL activo en el puerto `5432`.

2. **Configurar la Base de Datos:**
   ```bash
   # Crear base de datos en PostgreSQL
   psql -U postgres -c "CREATE DATABASE fincas_spring_db;"

   # Ejecutar esquema DDL y datos de prueba DML
   psql -U postgres -d fincas_spring_db -f sql/01_schema.sql
   psql -U postgres -d fincas_spring_db -f sql/02_data.sql
   ```

3. **Verificar Credenciales en `src/main/resources/application.properties`:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/fincas_spring_db
   spring.datasource.username=postgres
   spring.datasource.password=postgrespassword
   ```

4. **Ejecutar Pruebas y Levantar:**
   ```bash
   mvn clean test
   mvn spring-boot:run
   ```

Abrir el navegador web en: **`http://localhost:8080/login`**.

---

## 7. Despliegue en la Nube

* **Repositorio GitHub:** [https://github.com/YhonBarriosGandara/Spring_Boot_MVC_Thymeleaf](https://github.com/YhonBarriosGandara/Spring_Boot_MVC_Thymeleaf)
* **Aplicación Desplegada en Render:** [https://spring-boot-mvc-thymeleaf.onrender.com](https://spring-boot-mvc-thymeleaf.onrender.com)
* **Base de Datos en la Nube:** PostgreSQL Serverless en AWS (Neon Cloud) con SSL obligatorio.
