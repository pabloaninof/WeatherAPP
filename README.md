# 🌤️ Aplicación del Clima (Weather App) - Práctica Universitaria

Aplicación móvil nativa para Android desarrollada con **Kotlin**, **Jetpack Compose**, **Material 3**, **Navigation Compose**, **MVVM**, **Room Database** e **Inyección de Dependencias**.

---

## 📖 Índice

1. [Introducción](#1-introducción)
2. [Diseño de la Aplicación](#2-diseño-de-la-aplicación)
3. [Base de Datos Empleada](#3-base-de-datos-empleada)
4. [División del Trabajo](#4-división-del-trabajo)
5. [Desarrollo de la Aplicación](#5-desarrollo-de-la-aplicación)
6. [Problemas Encontrados y Soluciones](#6-problemas-encontrados-y-soluciones)
7. [Puntos Fuertes y Puntos Débiles](#7-puntos-fuertes-y-puntos-débiles)
8. [Conclusiones y Vías Futuras](#8-conclusiones-y-vías-futuras)
9. [Uso de la Inteligencia Artificial](#9-uso-de-la-inteligencia-artificial)

---

## 1. Introducción

Esta aplicación ha sido desarrollada como práctica universitaria para la asignatura de Desarrollo de Aplicaciones Móviles en Android. El objetivo principal es ofrecer una experiencia moderna, fluida y completa para la consulta del tiempo meteorológico en diversas ciudades del mundo, incorporando autenticación de usuarios, persistencia local de ciudades favoritas y funcionalidades avanzadas como mapas de estaciones y gráficos de temperatura.

---

## 2. Diseño de la Aplicación

La interfaz de usuario sigue strictly las directrices de **Material 3** e implementa **Jetpack Compose** para la declaración reactiva de UI. 

### Pantallas Principales (6 Destinos):
- **Pantalla de Login (`LoginScreen`)**: Formulario de acceso con validación de credenciales.
- **Pantalla de Registro (`RegisterScreen`)**: Creación de nueva cuenta si no se está autenticado.
- **Pantalla de Inicio (`HomeScreen`)**: Muestra el clima actual, pronóstico por horas en `LazyRow` y ciudades destacadas en `LazyColumn`.
- **Pantalla de Detalle (`DetailScreen`)**: Análisis detallado del clima pasando la ciudad como argumento mediante `Navigation Compose`.
- **Pantalla de Perfil/Ajustes (`ProfileScreen`)**: Gestión del usuario, cambio de unidades (°C / °F) y ajuste de tema.
- **Pantalla de Funcionalidades Avanzadas (`AdvancedScreen`)**: Mapa interactivo de estaciones meteorológicas, gráficos visuales de precipitación/temperatura y gestión de permisos de ubicación.

---

## 3. Base de Datos Empleada

Se utiliza **Room Database** (`AppDatabase`) para la persistencia local de datos en el dispositivo.

### Tablas y Entidades:
- **`UserEntity`**: Almacena el `id`, `username`, `email`, `passwordHash` y la unidad de temperatura preferida (`preferredTempUnit`) del usuario registrado.
- **`FavoriteCityEntity`**: Registra las ciudades guardadas como favoritas por el usuario con su nombre (`cityName`), país (`country`), temperatura (`temperatureC`), descripción del clima (`condition`) e icono (`iconRes`).

### Interfaces DAO:
- **`UserDao`**: Permite la inserción, consulta por email/password y actualización del usuario.
- **`FavoriteCityDao`**: Permite guardar ciudades en favoritos, eliminarlas y consultar si una ciudad es favorita mediante flujos reactivos (`Flow`).

---

## 4. División del Trabajo

- **Desarrollador Principal**: Implementación de arquitectura MVVM, diseño de pantallas Compose, configuración de Room Database, integración de Navigation Compose y documentación.

---

## 5. Desarrollo de la Aplicación

La aplicación implementa la arquitectura recomendada por Google (**MVVM + Repositorios**):
- **Modelos de Datos (`data/model`)**:
  - `CityWeather`, `HourlyForecast`, `DailyForecast` y el enum `WeatherCondition`.
- **Patrón Repository (`data/repository`)**:
  - **`WeatherRepository`**: Proveedor de datos meteorológicos, información por horas, días y sincronización con las ciudades favoritas de Room.
  - **`AuthRepository`**: Lógica de registro, autenticación y persistencia de sesión con Room.
- **Capa de Dominio / Presentación**: `ViewModels` que exponen `StateFlow` para gestionar `UiState` de forma reactiva.
- **Capa de Vista**: Composables modulares y reactivos adaptables a diferentes tamaños de pantalla.

---

## 6. Problemas Encontrados y Soluciones

1. **Gestión del Estado de Navegación**: Se solucionó mediante el uso de `Navigation Compose` pasando argumentos codificados en las rutas.
2. **Persistencia asíncrona**: Uso de Kotlin Coroutines y `Flow` para evitar bloqueos en el hilo principal de la UI.
3. **Solicitud de Permisos**: Implementación de `rememberPermissionState` para gestionar permisos de localización en tiempo de ejecución.

---

## 7. Puntos Fuertes y Puntos Débiles

### Puntos Fuertes:
- Interfaz moderna y atractiva construida 100% en Jetpack Compose con Material 3.
- Arquitectura limpia y escalable (MVVM + Repositorios + Room).
- Código totalmente documentado y dividido en commits incrementales.

### Puntos Débiles:
- Los datos meteorológicos actuales son simulados mediante un repositorio Mock (fácilmente ampliable a una API REST como Open-Meteo).

---

## 8. Conclusiones y Vías Futuras

El desarrollo de esta práctica ha permitido afianzar los conceptos fundamentales de Android moderno. Como vías futuras se contempla la integración de una API de clima en tiempo real y notificaciones push de alertas meteorológicas.

---

## 9. Uso de la Inteligencia Artificial

Para la realización de esta práctica se ha utilizado un asistente de Inteligencia Artificial como herramienta de apoyo, conforme a lo permitido en las bases de la asignatura.

### Usos de la IA en el proyecto:
- **Planificación e Ingeniería de Requisitos**: Estructuración del plan de desarrollo paso a paso por commits.
- **Diseño Arquitectónico**: Asesoramiento en la organización de paquetes (MVVM, Repositorio, Room).
- **Generación de Plantillas y Código**: Ayuda en la sintaxis de Jetpack Compose, configuración de dependencias de Gradle y Room DAO.
- **Documentación y Calidad**: Redacción técnica del `README.md` y revisión de buenas prácticas.

---

*Desarrollado para la Asignatura de Desarrollo Móvil.*
