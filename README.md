# WoodTrack-App

Aplicación móvil nativa en Android (Kotlin) para el chofer de WoodTrack.

## Descripción
Permite al chofer consultar las entregas que tiene asignadas, actualizar su estado
(en camino, entregado, incidencia), y registrar evidencia de entrega mediante
fotografía, nombre de quien recibe y firma digital de confirmación.

## Conexión con el backend
Esta app consume la API REST expuesta por el backend de WoodTrack (Laravel),
mediante Retrofit. No comparte código con el repositorio web — ambos son
independientes y se relacionan únicamente a través de la API.

Repositorio del backend/web: https://github.com/CrabF-ui/WoodTrack

## Flujo de trabajo (GitHub Flow)
- `main` siempre estable y protegida.
- Ramas de corta duración: `feature/`, `bugfix/`, `hotfix/`, `refactor/`.
- Pull Request obligatorio, mínimo 1 aprobación de alguien distinto al autor.
- Fusión mediante Squash and Merge.

## Equipo
- Desarrollo móvil y QA (UX/UI): Carlos de Lira Ibarra, César Alejandro Santiago Santos
