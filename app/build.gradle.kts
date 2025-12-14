plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.akillikampusmobil"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.akillikampusmobil"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // --- DÜZELTİLEN KISIM BURASI ---
    // Tek bir buildFeatures bloğu olmalı
    buildFeatures {
        compose = true
        viewBinding = true
    }

    // composeOptions da ana android bloğunun içinde olmalı
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    // --------------------------------
}

dependencies {
    // Standart Kütüphaneler
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testler
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Arkadaşının Compose kodları için gerekenler:
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    // (Diğer compose kütüphaneleri yukarıda zaten var ama bunlar kalabilir, sorun çıkarmaz)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // --- HELİN'İN KODU (HARİTA) İÇİN GEREKEN ---
    implementation("org.maplibre.gl:android-sdk:12.2.1")

}