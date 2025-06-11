import java.util.Properties
import java.io.FileInputStream
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.23"
    id("org.jetbrains.kotlin.kapt") version "1.9.23"
    id("com.google.dagger.hilt.android")
}

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

val githubClientId = localProperties["GITHUB_CLIENT_ID"] as String
val githubClientSecret = localProperties["GITHUB_CLIENT_SECRET"] as String


android {
    namespace = "com.example.softwareproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.softwareproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "GITHUB_CLIENT_ID", "\"$githubClientId\"")
        buildConfigField("String", "GITHUB_CLIENT_SECRET", "\"$githubClientSecret\"")
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.ui:ui-graphics:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.room:room-common:2.6.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    implementation("androidx.browser:browser:1.7.0")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0")
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-okhttp:2.3.8")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-compiler:2.51")
    add("kotlinCompilerPluginClasspath", "androidx.compose.compiler:compiler:1.5.13")
    
    implementation("com.google.android.material:material:1.11.0") // 최신 버전 확인
    // Navigation Component를 사용하려면 다음도 추가
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7") // 최신 버전 확인
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")     // 최신 버전 확인
    // Coil for image loading
    implementation("io.coil-kt:coil:2.6.0") // 최신 버전 확인

    // Coil's SVG Decoder (AndroidSVG를 내부적으로 사용하거나 유사한 기능 제공)
    implementation("io.coil-kt:coil-svg:2.6.0") // coil과 동일한 버전 사용
}