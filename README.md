# PrimeraAplicacion

Aplicación Android en Kotlin con tres pantallas (Main, Login, Second) construida sobre **Clean Architecture** con separación estricta de responsabilidades por capas: `ui`, `domain`, `data`, `core` y `utils`.

---

## Tabla de contenidos

1. [Descripcion general](#descripcion-general)
2. [Requisitos](#requisitos)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Arquitectura](#arquitectura)
5. [Capa core](#capa-core)
6. [Capa domain](#capa-domain)
7. [Capa data](#capa-data)
8. [Capa ui](#capa-ui)
9. [Capa utils](#capa-utils)
10. [Layouts](#layouts)
11. [Dependencias](#dependencias)
12. [Problemas resueltos](#problemas-resueltos)
13. [Como ejecutar](#como-ejecutar)

---

## Descripcion general

Proyecto Android nativo que demuestra la navegación entre pantallas aplicando el patrón **MVVM + Clean Architecture**. La UI observa estados inmutables (`sealed class`), el dominio contiene la lógica de negocio pura sin dependencias de Android, y la capa de datos implementa los contratos definidos por el dominio.

---

## Requisitos

| Herramienta        | Version           |
|--------------------|-------------------|
| Android Studio     | Ladybug o superior|
| Kotlin             | 2.2.10            |
| AGP                | 9.2.1             |
| compileSdk         | 36                |
| minSdk             | 31 (Android 12)   |
| targetSdk          | 36                |
| JVM                | Java 11           |

---

## Estructura del proyecto

```
PrimeraAplicacion/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/primeraaplicacion/
│   │   │   │
│   │   │   ├── core/
│   │   │   │   ├── base/
│   │   │   │   │   ├── BaseActivity.kt
│   │   │   │   │   ├── BaseFragment.kt
│   │   │   │   │   └── BaseViewModel.kt
│   │   │   │   ├── constants/
│   │   │   │   │   └── AppConstants.kt
│   │   │   │   ├── error/
│   │   │   │   │   └── AppError.kt
│   │   │   │   └── network/
│   │   │   │       └── NetworkConfig.kt
│   │   │   │
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   └── ScreenLocalDataSource.kt
│   │   │   │   ├── mapper/
│   │   │   │   │   └── ScreenMapper.kt
│   │   │   │   ├── remote/
│   │   │   │   │   └── ScreenRemoteDataSource.kt
│   │   │   │   └── repository/
│   │   │   │       └── ScreenRepositoryImpl.kt
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   └── Screen.kt
│   │   │   │   ├── repository/
│   │   │   │   │   └── IScreenRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       └── GetScreensUseCase.kt
│   │   │   │
│   │   │   ├── ui/
│   │   │   │   ├── main/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── MainViewModel.kt
│   │   │   │   │   ├── MainViewModelFactory.kt
│   │   │   │   │   └── MainUiState.kt
│   │   │   │   ├── second/
│   │   │   │   │   └── SecondActivity.kt
│   │   │   │   └── login/
│   │   │   │       ├── LoginActivity.kt
│   │   │   │       ├── LoginViewModel.kt
│   │   │   │       └── LoginUiState.kt
│   │   │   │
│   │   │   └── utils/
│   │   │       ├── Extensions.kt
│   │   │       ├── Validators.kt
│   │   │       └── DateFormatter.kt
│   │   │
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_second.xml
│   │       │   └── activity_login.xml
│   │       └── values/
│   │           └── themes.xml
│   └── build.gradle.kts
└── gradle/
    └── libs.versions.toml
```

---

## Arquitectura

La aplicacion sigue **Clean Architecture** con tres capas concéntricas y la regla de dependencia: las capas externas dependen de las internas, nunca al revés.

```
┌─────────────────────────────────────────────────────┐
│                        UI                           │
│        (Activities, ViewModels, UiStates)           │
│                         │                           │
│                         ▼ llama                     │
│  ┌──────────────────────────────────────────────┐   │
│  │                   DOMAIN                     │   │
│  │   (UseCases, IRepository, Models)            │   │
│  │                         ▲ implementa         │   │
│  │  ┌───────────────────────────────────────┐   │   │
│  │  │                DATA                   │   │   │
│  │  │  (RepositoryImpl, DataSources,        │   │   │
│  │  │   Mappers)                            │   │   │
│  │  └───────────────────────────────────────┘   │   │
│  └──────────────────────────────────────────────┘   │
│                                                     │
│  CORE (BaseActivity, BaseViewModel, ErrorHandler)   │
│  UTILS (Validators, DateFormatter, Extensions)      │
└─────────────────────────────────────────────────────┘

Regla: Domain no conoce a UI ni a Data.
       Data solo conoce a Domain (via interfaz).
       UI solo conoce a Domain (via UseCase).
```

### Patron MVVM por pantalla

```
Activity  ──observa──►  ViewModel  ──llama──►  UseCase  ──usa──►  IRepository
   │                       │                                           │
   │                   UiState                               RepositoryImpl
   │                  (sealed)                              /          \
   └──reacciona                                    RemoteDataSource  LocalDataSource
```

---

## Capa core

Contiene componentes compartidos por toda la aplicacion. Ninguna otra capa depende de `core`; `core` puede ser usado por cualquiera.

### BaseActivity.kt

```kotlin
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
```

Clase abstracta base de todas las Activities. Centraliza el comportamiento comun del ciclo de vida. Todas las Activities del proyecto heredan de ella en lugar de `AppCompatActivity` directamente.

---

### BaseFragment.kt

```kotlin
abstract class BaseFragment : Fragment() {
    protected abstract fun setupUI()
    protected abstract fun observeViewModel()
}
```

Clase abstracta base para Fragments. Obliga a cada Fragment concreto a implementar `setupUI()` (inicialización de vistas) y `observeViewModel()` (suscripción a LiveData), garantizando una estructura uniforme.

---

### BaseViewModel.kt

```kotlin
abstract class BaseViewModel : ViewModel() {
    private val _error = MutableLiveData<AppError?>()
    val error: LiveData<AppError?> = _error

    protected fun setError(error: AppError) { _error.value = error }
    protected fun clearError() { _error.value = null }
}
```

ViewModel abstracto base. Expone un canal de errores (`AppError`) centralizado que todos los ViewModels concretos heredan. Evita duplicar el manejo de errores en cada ViewModel.

---

### AppConstants.kt

```kotlin
object AppConstants {
    const val BASE_URL = "https://api.example.com/"
    const val TIMEOUT_SECONDS = 30L
    const val DATABASE_NAME = "primera_aplicacion_db"
    const val DATABASE_VERSION = 1
}
```

Constantes globales de la aplicación: URL base de la API, timeout de red, nombre y versión de la base de datos local.

---

### AppError.kt + ErrorHandler

```kotlin
sealed class AppError(val message: String) {
    class NetworkError(message: String = "Sin conexión a internet") : AppError(message)
    class ServerError(message: String = "Error en el servidor") : AppError(message)
    class UnknownError(message: String = "Error desconocido") : AppError(message)
}

object ErrorHandler {
    fun handle(throwable: Throwable): AppError = when (throwable) {
        is java.net.UnknownHostException -> AppError.NetworkError()
        is java.net.SocketTimeoutException -> AppError.NetworkError("Tiempo de espera agotado")
        else -> AppError.UnknownError(throwable.message ?: "Error desconocido")
    }
}
```

`AppError` es una `sealed class` que tipifica todos los errores posibles. `ErrorHandler` convierte excepciones de bajo nivel (`UnknownHostException`, `SocketTimeoutException`) en errores de dominio comprensibles para la UI.

---

### NetworkConfig.kt

```kotlin
object NetworkConfig {
    val baseUrl: String get() = AppConstants.BASE_URL
    val timeoutSeconds: Long get() = AppConstants.TIMEOUT_SECONDS
    // Punto de integración Retrofit — ver comentario en el archivo
}
```

Centraliza la configuración de red. Cuando se integre Retrofit, aquí se instanciará el cliente HTTP y el objeto `Retrofit`.

---

## Capa domain

Capa más interna. **No tiene dependencias de Android ni de frameworks externos.** Solo Kotlin puro. Es la más estable: puede cambiar `data` o `ui` sin tocar `domain`.

### Screen.kt — Modelo de negocio

```kotlin
data class Screen(
    val id: Int,
    val title: String
)
```

Representa una pantalla de la aplicacion como entidad de negocio. Es independiente de Android; puede usarse en tests unitarios puros de JVM.

---

### IScreenRepository.kt — Contrato del repositorio

```kotlin
interface IScreenRepository {
    fun getScreens(): List<Screen>
    fun getScreenById(id: Int): Screen?
}
```

Define **qué** operaciones de datos necesita el dominio, sin especificar **cómo** se obtienen. La capa `data` implementa este contrato. Esta inversión de dependencia es el núcleo de Clean Architecture.

---

### GetScreensUseCase.kt — Caso de uso

```kotlin
class GetScreensUseCase(private val repository: IScreenRepository) {
    operator fun invoke(): List<Screen> = repository.getScreens()
}
```

Encapsula la operación de negocio "obtener lista de pantallas". La UI siempre llama al UseCase, nunca al repositorio directamente. El operador `invoke` permite llamarlo como función: `getScreensUseCase()`.

---

## Capa data

Implementa los contratos del dominio. Se comunica con fuentes remotas (API REST) y locales (Room). La UI nunca toca esta capa directamente.

### ScreenRepositoryImpl.kt

```kotlin
class ScreenRepositoryImpl(
    private val remoteDataSource: ScreenRemoteDataSource,
    private val localDataSource: ScreenLocalDataSource
) : IScreenRepository {

    override fun getScreens(): List<Screen> {
        val cached = localDataSource.getScreens()
        if (cached.isNotEmpty()) return cached
        val remote = remoteDataSource.getScreens()
        localDataSource.saveScreens(remote)
        return remote
    }

    override fun getScreenById(id: Int): Screen? =
        localDataSource.getScreens().find { it.id == id }
            ?: remoteDataSource.getScreens().find { it.id == id }
}
```

Estrategia **cache-first**: sirve datos locales si existen; si no, consulta el remoto, persiste en local y retorna. Implementa `IScreenRepository`, por lo que el dominio nunca conoce esta clase concreta.

---

### ScreenRemoteDataSource.kt

```kotlin
class ScreenRemoteDataSource {
    // Punto de integración Retrofit:
    // @GET("screens") suspend fun fetchScreens(): Response<List<ScreenDto>>
    fun getScreens(): List<Screen> = listOf(
        Screen(id = 1, title = "Pantalla Principal"),
        Screen(id = 2, title = "Segunda Pantalla")
    )
}
```

Fuente de datos remota. Actualmente retorna datos de ejemplo. Al integrar Retrofit, se reemplaza el cuerpo del método por la llamada a la API, manteniendo la misma firma.

---

### ScreenLocalDataSource.kt

```kotlin
class ScreenLocalDataSource {
    // Punto de integración Room:
    // @Dao interface ScreenDao { @Query("SELECT * FROM screens") fun getAll(): List<ScreenEntity> }
    private val cache = mutableListOf<Screen>()

    fun getScreens(): List<Screen> = cache.toList()
    fun saveScreens(screens: List<Screen>) { cache.clear(); cache.addAll(screens) }
}
```

Fuente de datos local. Actualmente usa un caché en memoria. Al integrar Room, se reemplaza por un `@Dao`, manteniendo la misma interfaz pública.

---

### ScreenMapper.kt

```kotlin
object ScreenMapper {
    fun fromMap(raw: Map<String, Any>): Screen = Screen(
        id = (raw["id"] as? Int) ?: 0,
        title = (raw["title"] as? String) ?: ""
    )

    fun toMap(screen: Screen): Map<String, Any> = mapOf(
        "id" to screen.id,
        "title" to screen.title
    )
}
```

Convierte entre representaciones de datos (DTO de API o entidad de Room) y el modelo de dominio `Screen`. Aísla la UI y el dominio de los cambios en el formato de la API.

---

## Capa ui

Solo maneja presentación. Observa `LiveData`, reacciona a estados y delega toda la lógica al ViewModel. **No contiene reglas de negocio.**

### MainUiState.kt

```kotlin
sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val screens: List<Screen>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}
```

Estado completo de la pantalla principal. El `when` exhaustivo sobre esta `sealed class` en la Activity garantiza que todos los casos están manejados en tiempo de compilación.

---

### MainViewModel.kt

```kotlin
class MainViewModel(private val getScreensUseCase: GetScreensUseCase) : BaseViewModel() {

    private val _uiState = MutableLiveData<MainUiState>(MainUiState.Loading)
    val uiState: LiveData<MainUiState> = _uiState

    fun loadScreens() {
        _uiState.value = MainUiState.Loading
        runCatching { getScreensUseCase() }
            .onSuccess { _uiState.value = MainUiState.Success(it) }
            .onFailure {
                setError(ErrorHandler.handle(it))
                _uiState.value = MainUiState.Error(it.message ?: "Error desconocido")
            }
    }
}
```

Recibe el UseCase por constructor (inyección manual). Expone `uiState` como `LiveData` inmutable. Usa `runCatching` para manejar errores de forma funcional y delega la clasificación del error a `ErrorHandler`.

---

### MainViewModelFactory.kt

```kotlin
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val localDataSource = ScreenLocalDataSource()
        val remoteDataSource = ScreenRemoteDataSource()
        val repository = ScreenRepositoryImpl(remoteDataSource, localDataSource)
        val useCase = GetScreensUseCase(repository)
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(useCase) as T
    }
}
```

Ensambla manualmente el grafo de dependencias: `DataSources → Repository → UseCase → ViewModel`. Cuando se integre **Hilt**, esta factory desaparece y la inyección es automática.

---

### MainActivity.kt

```kotlin
class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MainViewModelFactory())[MainViewModel::class.java]

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MainUiState.Loading -> { /* mostrar indicador */ }
                is MainUiState.Success -> Toast.makeText(this,
                    "${state.screens.size} pantallas cargadas", Toast.LENGTH_SHORT).show()
                is MainUiState.Error -> Toast.makeText(this,
                    state.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadScreens()

        findViewById<Button>(R.id.btnNavigate).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}
```

Hereda de `BaseActivity`. Instancia el ViewModel con la factory, observa el estado y reacciona. El botón navega a `SecondActivity` via `Intent` explícito.

---

### LoginUiState.kt

```kotlin
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
```

Cuatro estados del flujo de login: inactivo, cargando, exitoso o con error de validación.

---

### LoginViewModel.kt

```kotlin
class LoginViewModel : BaseViewModel() {

    private val _uiState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (!Validators.isValidUsername(username)) {
            _uiState.value = LoginUiState.Error("El usuario debe tener al menos 3 caracteres")
            return
        }
        if (!Validators.isValidPassword(password)) {
            _uiState.value = LoginUiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        _uiState.value = LoginUiState.Loading
        // Punto de integración: llamar al UseCase de autenticación
        _uiState.value = LoginUiState.Success
    }
}
```

Valida campos usando `Validators` (capa `utils`) antes de proceder. No tiene factory porque no recibe dependencias en el constructor; `ViewModelProvider` lo crea directamente.

---

### LoginActivity.kt

```kotlin
class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        viewModel.uiState.observe(this) { state ->
            when (state) {
                is LoginUiState.Idle    -> {}
                is LoginUiState.Loading -> {}
                is LoginUiState.Success -> Toast.makeText(this,
                    "Login exitoso", Toast.LENGTH_SHORT).show()
                is LoginUiState.Error   -> Toast.makeText(this,
                    state.message, Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            viewModel.login(etUsername.text.toString(), etPassword.text.toString())
        }
    }
}
```

---

### SecondActivity.kt

```kotlin
class SecondActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}
```

Pantalla de destino simple. Hereda de `BaseActivity` y solo infla su layout.

---

## Capa utils

Funciones puras y helpers reutilizables. No dependen de la arquitectura ni de ninguna capa específica.

### Extensions.kt

```kotlin
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
```

Extensión de `Context` para mostrar Toasts con una sola línea desde cualquier Activity o Fragment.

---

### Validators.kt

```kotlin
object Validators {
    fun isValidUsername(username: String): Boolean =
        username.isNotBlank() && username.length >= 3

    fun isValidPassword(password: String): Boolean =
        password.length >= 6

    fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
```

| Función | Regla |
|---|---|
| `isValidUsername` | No vacío y mínimo 3 caracteres |
| `isValidPassword` | Mínimo 6 caracteres |
| `isValidEmail` | Formato válido según `Patterns.EMAIL_ADDRESS` |

---

### DateFormatter.kt

```kotlin
object DateFormatter {
    private const val DEFAULT_PATTERN = "dd/MM/yyyy"
    private const val FULL_PATTERN    = "dd/MM/yyyy HH:mm"

    fun format(date: Date, pattern: String = DEFAULT_PATTERN): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(date)

    fun formatFull(date: Date): String = format(date, FULL_PATTERN)

    fun today(): String = format(Date())
}
```

| Función | Formato | Ejemplo |
|---|---|---|
| `format(date)` | `dd/MM/yyyy` | `30/05/2026` |
| `formatFull(date)` | `dd/MM/yyyy HH:mm` | `30/05/2026 14:35` |
| `today()` | `dd/MM/yyyy` | fecha actual |

---

## Layouts

### activity_main.xml

`LinearLayout` vertical centrado con:
- `TextView` (id: `tvWelcome`) — texto `"¡Hola Mundo 1!"`, tamaño `18sp`
- `Button` (id: `btnNavigate`) — texto `"Segunda Pantalla"`, navega a `SecondActivity`

### activity_second.xml

`LinearLayout` vertical centrado con:
- `TextView` — texto `"¡Hola Mundo 2!"`, tamaño `18sp`

### activity_login.xml

`LinearLayout` vertical centrado con padding `24dp`:
- `TextView` — título `"Login"`, tamaño `24sp`
- `EditText` (id: `etUsername`) — hint `"Usuario"`
- `EditText` (id: `etPassword`) — hint `"Contraseña"`, `inputType="textPassword"`
- `Button` (id: `btnLogin`) — texto `"Ingresar"`

### themes.xml

```xml
<style name="Theme.PrimeraAplicacion" parent="Theme.AppCompat.Light.NoActionBar" />
```

Hereda de `Theme.AppCompat`, requerido para que `AppCompatActivity` no lance excepción en tiempo de ejecución.

---

## Dependencias

### app/build.gradle.kts

```kotlin
dependencies {
    implementation(libs.androidx.appcompat)                   // AppCompatActivity
    implementation(libs.androidx.lifecycle.viewmodel.ktx)    // ViewModel
    implementation(libs.androidx.lifecycle.livedata.ktx)     // LiveData
    implementation(libs.androidx.fragment.ktx)               // BaseFragment
    implementation(libs.androidx.core.ktx)                   // Extensiones Kotlin
    implementation(libs.androidx.lifecycle.runtime.ktx)      // Ciclo de vida
}
```

### gradle/libs.versions.toml

| Alias | Grupo | Versión |
|---|---|---|
| `androidx.appcompat` | `androidx.appcompat:appcompat` | 1.7.0 |
| `androidx.lifecycle.viewmodel.ktx` | `androidx.lifecycle:lifecycle-viewmodel-ktx` | 2.6.1 |
| `androidx.lifecycle.livedata.ktx` | `androidx.lifecycle:lifecycle-livedata-ktx` | 2.6.1 |
| `androidx.fragment.ktx` | `androidx.fragment:fragment-ktx` | 1.6.2 |
| `androidx.core.ktx` | `androidx.core:core-ktx` | 1.10.1 |

### Integraciones futuras

| Librería | Donde | Para qué |
|---|---|---|
| Retrofit + Gson | `data/remote/` | Consumo real de API REST |
| Room | `data/local/` | Persistencia local en SQLite |
| Hilt | Todo el proyecto | Inyección de dependencias automática (reemplaza las factories) |
| Navigation Component | `ui/` | Navegación declarativa con backstack gestionado |

---

## Problemas resueltos

| Problema | Causa | Solución |
|---|---|---|
| `Unresolved reference 'appcompat'` | Dependencia `androidx.appcompat` no declarada | Agregada en `libs.versions.toml` y `build.gradle.kts` |
| App se cerraba al iniciar | Tema heredaba de `android:Theme.Material` | Cambiado `parent` en `themes.xml` a `Theme.AppCompat.Light.NoActionBar` |

---

## Como ejecutar

1. Abrir el proyecto en **Android Studio**.
2. Esperar sincronización de Gradle o forzarla: `File > Sync Project with Gradle Files`.
3. Seleccionar emulador con **API 31 o superior**.
4. Presionar **Run > Run 'app'** o `Shift + F10`.
