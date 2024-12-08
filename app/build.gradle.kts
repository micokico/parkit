plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") // Plugin do Google Services
}

android {
    namespace = "com.example.parkit"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.parkit"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation ("com.google.firebase:firebase-auth:22.1.2")

    implementation ("com.firebaseui:firebase-ui-auth:8.0.0")

    implementation("org.osmdroid:osmdroid-android:6.1.14")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.graphics.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dependências do Firebase (com Firebase BOM já configurado)
    implementation("com.google.firebase:firebase-auth") // Autenticação Firebase
    implementation("com.google.firebase:firebase-database") // Banco de dados Realtime do Firebase (se precisar)
    implementation("com.google.firebase:firebase-firestore") // Firestore, caso queira usar (opcional)

    // Para usar o Firebase Analytics (opcional)
    implementation("com.google.firebase:firebase-analytics")

    // Para usar o Firebase Storage
    implementation("com.google.firebase:firebase-storage") // Firebase Storage

    // O Firebase BOM já foi incluído, então as versões das dependências são gerenciadas automaticamente
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

}

// Aplica o plugin do Google Services no final
apply(plugin = "com.google.gms.google-services")
