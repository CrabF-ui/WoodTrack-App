# WoodTrack-App

Aplicación móvil nativa en Android (Kotlin + Jetpack Compose) para el **chofer** de WoodTrack.

## Descripción
Permite al chofer consultar las entregas que tiene asignadas, actualizar su estado
(pendiente o en camino), registrar incidencias y registrar la entrega con evidencia:
fotografías, nombre de quien recibe y firma digital de confirmación.

### Funciones
- **Inicio de sesión** con correo y contraseña (token de Laravel Sanctum).
- **Lista de entregas** pendientes y en camino, con la siguiente entrega marcada.
- **Detalle de entrega** con cliente, dirección, notas y botón para abrir **Google Maps**.
- **Actualizar estado** entre pendiente y en camino.
- **Registrar incidencia** con motivo y notas.
- **Registrar entrega** con 1 a 3 fotos, nombre de quien recibe y firma (o motivo si no firma).
- **Confirmación** en pantalla al registrar una entrega o una incidencia.
- **Finalizadas**: consulta de entregas entregadas y con incidencia, con filtros por hoy, semana o fecha, y por tipo.
- **Notificación** cuando se asigna una entrega nueva (revisión en segundo plano cada ~15 minutos).
- **Cerrar sesión** y regreso automático al inicio de sesión si el token vence.

## Conexión con el backend
Esta app consume la API REST del backend de WoodTrack (Laravel) mediante Retrofit.
No comparte código con el repositorio web: ambos son independientes y se relacionan
únicamente a través de la API.

- Repositorio del backend/web: https://github.com/CrabF-ui/WoodTrack
- Detalle de los endpoints: archivo `docs/api.md` del repositorio del backend.

---

## Manual de instalación

### 1. Requisitos
- **Android Studio** (versión reciente).
- **Git**.
- El **backend de WoodTrack corriendo en tu computadora**. Sigue la *Guía para levantar el backend*
  del equipo (PHP 8.4.1 o superior, Composer, SQLite). Sin el backend la app no puede iniciar sesión.
- Un **celular Android** (7.0 o superior) con depuración USB, o un **emulador** de Android Studio.

### 2. Clonar el repositorio
**Desde Android Studio:** pantalla de inicio → **Clone Repository** (o *File → New → Project from Version Control*)
y pega la URL:
```
https://github.com/CrabF-ui/WoodTrack-App.git
```

**Desde la terminal:**
```bash
git clone https://github.com/CrabF-ui/WoodTrack-App.git
```
Luego en Android Studio: **File → Open** y elige la carpeta `WoodTrack-App`.

La primera vez Android Studio descarga Gradle y las librerías ("Importing Gradle project").
Puede tardar varios minutos; espera a que termine.

### 3. Levantar el backend
En la carpeta del backend (no en la de la app):

| Vas a usar | Comando |
| --- | --- |
| Emulador | `php artisan serve` |
| Celular físico | `php artisan serve --host=0.0.0.0 --port=8000` |

Deja esa terminal abierta mientras usas la app.

### 4. Configurar la dirección del servidor (IP)
La app necesita saber dónde está el backend. Hay que poner la **misma dirección en dos archivos**:

1. `app/src/main/java/com/example/logistica/data/remote/RetrofitClient.kt` → `BASE_URL`
2. `app/src/debug/res/xml/network_security_config.xml`

| Vas a usar | `BASE_URL` | En `network_security_config.xml` |
| --- | --- | --- |
| Emulador | `http://10.0.2.2:8000/api/` | `10.0.2.2` |
| Celular físico | `http://TU_IP:8000/api/` | `TU_IP` |

- `TU_IP` es la **Dirección IPv4** del adaptador **Wi‑Fi** que aparece al correr `ipconfig` en tu PC
  (no la de "vEthernet", "VirtualBox" o "WSL").
- `BASE_URL` siempre termina en `/`.
- Si solo la cambias en uno de los dos archivos, **no va a conectar**.

> ⚠️ **No subas tu IP al repositorio.** Cada integrante tiene una IP distinta. Antes de hacer commit,
> revisa que `RetrofitClient.kt` y `network_security_config.xml` no aparezcan como modificados, o
> regrésalos con `git restore <archivo>`.

### 5. Solo celular físico: permitir la conexión
1. El celular y la PC deben estar en el **mismo Wi‑Fi** (no datos móviles ni Wi‑Fi de invitados).
2. En Windows, tu red Wi‑Fi debe estar como **Privada**:
   *Configuración → Red e Internet → Wi‑Fi → tu red → Perfil de red: Privado*.
3. Abre el puerto 8000 en el firewall. En **PowerShell como administrador**:
   ```powershell
   New-NetFirewallRule -DisplayName "Laravel 8000" -Direction Inbound -Protocol TCP -LocalPort 8000 -Action Allow -Profile Private
   ```
4. **Prueba la conexión:** en el navegador del celular abre `http://TU_IP:8000/api/entregas`.
   Si aparece `{"message":"Unauthenticated."}`, la conexión funciona.

Algunas redes (como las de la escuela) bloquean la conexión entre dispositivos. Si todo está bien
y aun así no carga, prueba en otra red o con el hotspot de un celular.

### 6. Correr la app
1. **File → Sync Project with Gradle Files** (el ícono del elefante).
2. Conecta el celular por USB con la **depuración USB** activada (o abre un emulador).
3. Elige el dispositivo arriba y presiona **Run ▶**.

### 7. Iniciar sesión
Usuario de prueba que crea el backend con `php artisan migrate --seed`:

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Chofer | `chofer@woodtrack.test` | `password123` |

Las cuentas de administración e inventario son solo para el panel web; la app responde
"Esta cuenta no tiene acceso a la app".

### 8. Crear entregas de prueba
Con la base recién creada la lista sale vacía. Se pueden asignar entregas desde el **panel web**
(`admin@woodtrack.test`) o con **tinker** en la carpeta del backend (`php artisan tinker`):
```php
$chofer = \App\Models\User::where('email', 'chofer@woodtrack.test')->first();
$cliente = \App\Models\Cliente::first();
$pedido = \App\Models\Pedido::create(['cliente_id' => $cliente->id, 'fecha_pedido' => now()->toDateString(), 'fecha_entrega' => now()->toDateString(), 'estado' => 'completado', 'total' => 1000, 'direccion' => 'Calle de prueba 123']);
\App\Models\DetallePedido::create(['pedido_id' => $pedido->id, 'producto' => 'Mesa de pino', 'cantidad' => 1, 'precio_unitario' => 1000]);
\App\Models\Entrega::create(['pedido_id' => $pedido->id, 'chofer_id' => $chofer->id, 'fecha_asignada' => now()->toDateString(), 'orden' => 1, 'estado' => 'pendiente']);
```
Escribe `exit` para salir de tinker. En la app, toca **Sincronizar**.

---

## Mantener la app actualizada
Cuando se fusione un cambio en `main`:

**Sin comandos:** en Android Studio, estando en `main`: **Git → Update Project…** (`Ctrl + T`) → OK.

**Con la terminal:**
```bash
git stash          # guarda aparte tu IP si la tienes cambiada
git checkout main
git pull
git stash pop      # regresa tu IP
```
Después: **Sync Gradle** y **Run**.

> Cada vez que cambies de rama, haz **Sync Gradle**. Si aparecen errores en rojo como
> *"Cannot access class … ComposableFunction1"*, es Android Studio, no el código:
> **File → Sync Project with Gradle Files** y, si sigue, **File → Invalidate Caches → Invalidate and Restart**.

## Flujo de trabajo (GitHub Flow)
- `main` siempre estable y protegida.
- Ramas de corta duración: `feature/`, `bugfix/`, `hotfix/`, `refactor/` (incluir la clave de Jira, por ejemplo `feature/WOOD-70-entregas-finalizadas`).
- Pull Request obligatorio, mínimo 1 aprobación de alguien distinto al autor.
- Fusión mediante **Squash and Merge**.

### Pasos para una tarjeta nueva
```bash
git checkout main
git pull
git checkout -b feature/WOOD-XX-descripcion-corta
git push -u origin feature/WOOD-XX-descripcion-corta
```
Trabaja, prueba y haz commits:
```bash
git add .
git commit -m "WOOD-XX feat: qué se hizo"
git push
```
Abre el PR hacia `main`, pide la revisión y, cuando lo aprueben, **Squash and merge** → **Delete branch**.
Al final:
```bash
git checkout main
git pull
git branch -D feature/WOOD-XX-descripcion-corta
```

## Pruebas unitarias
```bash
./gradlew testDebugUnitTest
```
En Windows: `.\gradlew testDebugUnitTest`. El reporte queda en
`app/build/reports/tests/testDebugUnitTest/index.html`.

## Problemas frecuentes

| Síntoma | Qué hacer |
| --- | --- |
| Se queda en "Ingresando…" y luego *"No se pudo conectar con el servidor"* | El backend está apagado o la IP no es correcta. Revisa los pasos 3 a 5. |
| En el navegador del celular no abre `http://TU_IP:8000/api/entregas` | Servidor sin `--host=0.0.0.0`, red en "Pública", firewall o celular en otra red. |
| En el navegador sí abre pero la app no | Falta la IP en uno de los dos archivos del paso 4, o no se volvió a dar Run. |
| *"Las credenciales no son correctas."* | Correo o contraseña mal escritos. |
| *"Esta cuenta no tiene acceso a la app."* | Esa cuenta no es de chofer. Usa `chofer@woodtrack.test`. |
| *"Demasiados intentos…"* | Más de 5 intentos de login en un minuto. Espera un minuto. |
| *"Tu sesión expiró."* | El token venció o se revocó. Vuelve a iniciar sesión. |
| La lista sale vacía | No hay entregas asignadas a ese chofer. Créalas (paso 8) y toca Sincronizar. |
| No llegan las notificaciones | Pueden tardar hasta ~15 minutos. En Huawei: *Ajustes → Batería → Inicio de apps → WoodTrack*, permitir la ejecución en segundo plano. En Android 13+ acepta el permiso de notificaciones. |
| Al regresar de Maps sale "Cargando entrega…" | Es normal en celulares con poca memoria: la app se recarga sola. |
| `CONFLICT` al hacer `git pull` | No toques nada y avisa al equipo; casi siempre es por la IP. |

## Estructura del proyecto
```
app/src/main/java/com/example/logistica/
├── components/      Componentes visuales reutilizables (botones, tarjetas, barra inferior)
├── data/
│   ├── model/       Modelos de la API (Delivery, Pedido, LoginResponse…)
│   ├── remote/      Retrofit, ApiService, TokenManager, SesionEvents
│   └── repository/  DeliveryRepository (llamadas a la API y manejo de errores)
├── navigation/      AppNavigation (rutas entre pantallas)
├── screens/         Pantallas (login, lista, detalle, estado, incidencia, entrega, finalizadas…)
├── ui/theme/        Colores y tipografía (estilo del diseño de Figma)
├── util/            Fotos y firma (Evidencia), fechas (Fechas)
└── viewmodel/       LoginViewModel, DeliveryViewModel y estado de Finalizadas
app/src/main/java/notificaciones/   NotificationHelper y NuevasEntregasWorker
app/src/debug/                      Permiso de http:// solo para desarrollo
app/src/test/                       Pruebas unitarias
```

## Equipo
- Desarrollo móvil y QA (UX/UI): Carlos de Lira Ibarra, César Alejandro Santiago Santos
