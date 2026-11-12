plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.logistica"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.logistica"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.01.00"))
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Retrofit: conexión con la API REST
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

// Conversión JSON a objetos Kotlin
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

// OkHttp: comunicación HTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

// Interceptor para agregar el token de autenticación
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Corrutinas de Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

// ViewModel para Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")

// Estados de ciclo de vida
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
}