# Upskilling Gradle Project

Proyecto de testing con Karate Framework migrado de Maven a Gradle.

## Estructura del Proyecto

```
upskilling-gradle/
├── build.gradle.kts          # Configuración de Gradle
├── src/
│   └── test/
│       └── java/
│           ├── examples/
│           │   └── idiomas/
│           │       ├── IdiomasRunner.java
│           │       ├── post-idiomas.feature
│           │       ├── get-idiomas.feature
│           │       ├── put-idiomas.feature
│           │       └── delete-idiomas.feature
│           ├── karate-config.js
│           └── logback-test.xml
└── resources/
    └── openapi.yml
```

## Dependencias Principales

- **Karate JUnit5**: 1.5.0
- **Java**: 17
- **Testing**: JUnit5 Platform

## Comandos Gradle

### Ejecutar todos los tests
```bash
./gradlew test
```

### Ejecutar tests específicos
```bash
./gradlew test --tests examples.idiomas.IdiomasRunner
```

### Ejecutar un test específico
```bash
./gradlew test --tests examples.idiomas.IdiomasRunner.testPostIdiomas
```

### Limpiar y compilar
```bash
./gradlew clean build
```

## Configuración

El proyecto está configurado para:
- Java 17
- UTF-8 encoding
- JUnit5 Platform
- Karate Framework para testing de APIs REST

## Features de Testing

El proyecto incluye tests para el CRUD de idiomas:
- **POST**: Creación de idiomas
- **GET**: Consulta de idiomas por ID
- **PUT**: Actualización de idiomas
- **DELETE**: Eliminación de idiomas

Todos los tests apuntan al servidor de producción:
`https://labqa-api-rest-crud-basic-tqa.onrender.com/api/v1/idiomas`
