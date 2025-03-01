plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Google Services Gradle plugin
    id("com.google.gms.google-services")

    // Dependency Injection With Hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    // Google Maps SDK
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    // To add your Maps API key to this project:
    // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
    // 2. Add this line, where YOUR_API_KEY is your API key:
    //        MAPS_API_KEY=YOUR_API_KEY
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}

android {
    namespace = "com.hackaton.sustaina"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hackaton.sustaina"
        minSdk = 26
        targetSdk = 35
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.database)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Navigation Bar
    implementation("androidx.navigation:navigation-compose:2.5.3") // check for the latest version
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    // Dependency Injection Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    // Dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")

    // Constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    // CameraX
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")
    implementation("androidx.camera:camera-extensions:1.4.1")
    implementation("com.google.accompanist:accompanist-permissions:0.37.2")

    // Google Maps SDK
    implementation("com.google.maps.android:maps-compose:6.4.1")
    implementation("com.google.maps.android:maps-ktx:5.1.1")

    // Permissions Helper for Jetpack Compose
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Fused Location API; obtain current location of user
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Background location when app is not active screen // NOT IMPLEMENTED
//    implementation("androidx.work:work-runtime-ktx:2.10.0")

}

kapt {
    correctErrorTypes = true
}