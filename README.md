# PrimeraAplicacion

Aplicación Android de introducción que demuestra navegación entre dos pantallas usando Activities y Views con Kotlin.

---

## Descripcion general

Proyecto Android nativo que implementa una pantalla de bienvenida con un botón de navegación hacia una segunda pantalla. Sirve como base para aprender el ciclo de vida de Activities, layouts XML e Intents en Android.

---

## Estructura del proyecto

```
PrimeraAplicacion/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/primeraaplicacion/
│   │       │   ├── MainActivity.kt          # Pantalla 1 - entrada principal
│   │       │   └── SecondActivity.kt        # Pantalla 2 - destino de navegacion
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_main.xml    # Layout pantalla 1
│   │       │   │   └── activity_second.xml  # Layout pantalla 2
│   │       │   ├── values/
│   │       │   │   ├── themes.xml           # Tema de la aplicacion
│   │       │   │   ├── colors.xml
│   │       │   │   └── strings.xml
│   │       │   └── xml/
│   │       │       ├── backup_rules.xml
│   │       │       └── data_extraction_rules.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml                   # Catalogo de dependencias
└── build.gradle.kts
```

---

## Requisitos

| Herramienta        | Version        |
|--------------------|----------------|
| Android Studio     | Ladybug o superior |
| Kotlin             | 2.2.10         |
| AGP (Gradle Plugin)| 9.2.1          |
| compileSdk         | 36             |
| minSdk             | 31 (Android 12)|
| targetSdk          | 36             |
| JVM                | Java 11        |

---

## Dependencias principales

| Libreria                          | Version       | Uso                              |
|-----------------------------------|---------------|----------------------------------|
| `androidx.appcompat:appcompat`    | 1.7.0         | AppCompatActivity y temas        |
| `androidx.core:core-ktx`         | 1.10.1        | Extensiones Kotlin para Android  |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.6.1 | Ciclo de vida coroutines        |
| `androidx.compose:compose-bom`   | 2026.02.01    | BOM de Compose (incluido)        |

> Las dependencias de Compose estan presentes en el proyecto por la plantilla original pero no son utilizadas por las pantallas actuales.

---

## Arquitectura

La aplicacion utiliza el patron **Activity + View** (XML Layouts):

```
MainActivity
    └── setContentView(R.layout.activity_main)
            ├── TextView  (tvWelcome)   → muestra "¡Hola Mundo 1!"
            └── Button    (btnNavigate) → lanza SecondActivity via Intent

SecondActivity
    └── setContentView(R.layout.activity_second)
            └── TextView               → muestra "¡Hola Mundo 2!"
```

### Flujo de navegacion

```
[Pantalla 1 - MainActivity]
        |
        | usuario presiona "Segunda Pantalla"
        | Intent(this, SecondActivity::class.java)
        v
[Pantalla 2 - SecondActivity]
```

---

## Archivos fuente

### MainActivity.kt

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNavigate = findViewById<Button>(R.id.btnNavigate)
        btnNavigate.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
```

- Hereda de `AppCompatActivity` para compatibilidad con temas AppCompat.
- Usa `findViewById` para obtener la referencia al boton.
- Navega a `SecondActivity` mediante un `Intent` explicito.

---

### SecondActivity.kt

```kotlin
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}
```

- Pantalla de destino simple que solo infla su layout.

---

## Layouts

### activity_main.xml

- Contenedor: `LinearLayout` vertical, centrado (`gravity="center"`).
- `TextView` con id `tvWelcome`, texto `"¡Hola Mundo 1!"`, tamaño `18sp`.
- `Button` con id `btnNavigate`, texto `"Segunda Pantalla"`.

### activity_second.xml

- Contenedor: `LinearLayout` vertical, centrado.
- `TextView` con texto `"¡Hola Mundo 2!"`, tamaño `18sp`.

---

## Tema

Definido en `res/values/themes.xml`:

```xml
<style name="Theme.PrimeraAplicacion" parent="Theme.AppCompat.Light.NoActionBar" />
```

Hereda de `Theme.AppCompat.Light.NoActionBar`, requerido para que `AppCompatActivity` no crashee en tiempo de ejecucion.

---

## AndroidManifest.xml

Ambas Activities estan registradas:

```xml
<activity android:name=".MainActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
<activity android:name=".SecondActivity" />
```

---

## Problemas conocidos y soluciones aplicadas

| Problema | Causa | Solucion |
|----------|-------|----------|
| `Unresolved reference 'appcompat'` | Dependencia `androidx.appcompat` no declarada | Agregada en `libs.versions.toml` y `build.gradle.kts` |
| App se cerraba al iniciar | Tema heredaba de `android:Theme.Material` en lugar de `Theme.AppCompat` | Cambiado el `parent` en `themes.xml` a `Theme.AppCompat.Light.NoActionBar` |

---

## Como ejecutar

1. Abrir el proyecto en **Android Studio**.
2. Esperar a que Gradle sincronice (`File > Sync Project with Gradle Files`).
3. Seleccionar un emulador con API 31 o superior.
4. Presionar **Run > Run 'app'** o `Shift + F10`.
