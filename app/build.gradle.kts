plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf")
}

android {
    namespace = "com.vibe.homeapp"
    compileSdk = 36 // 2026 standard

    defaultConfig {
        applicationId = "com.vibe.homeapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    // 1. Wear OS Core & Compose
    implementation("androidx.wear.compose:compose-material3:1.4.0")
    implementation("androidx.wear.compose:compose-foundation:1.4.0")
    
    // 2. The Tiles 1.6 Library (The heart of the app)
    implementation("androidx.wear.tiles:tiles:1.6.0")
    implementation("androidx.wear.tiles:tiles-material:1.6.0")
    implementation("androidx.wear.tiles:tiles-tooling-preview:1.6.0")

    // 3. Network (Ktor 3.4.2)
    implementation("io.ktor:ktor-client-core:3.4.2")
    implementation("io.ktor:ktor-client-okhttp:3.4.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.4.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.2")

    // 4. DataStore & Proto (Storage)
    implementation("androidx.datastore:datastore:1.2.1")
    implementation("com.google.protobuf:protobuf-javalite:3.25.1")

    // 5. Standard Wear helpers
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
}

// Protobuf configuration for DataStore
protobuf {
    protoc { artifact = "com.google.protobuf:protoc:3.25.1" }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") { option("lite") }
            }
        }
    }
}
