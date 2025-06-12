import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application") version "8.8.2"
    id("org.jetbrains.kotlin.android") version "2.1.0"
    id("org.jetbrains.kotlin.kapt") version "2.1.0"
    id("com.google.dagger.hilt.android") version "2.55"
    //id("com.google.devtools.ksp") version "2.1.20-2.0.1"
    id("com.google.gms.google-services") version "4.4.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"

    id("com.google.firebase.crashlytics") version "2.9.9" // ✅ 이거 추가

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
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8" // 또는 1.6.0
    }

    applicationVariants.all {
        kotlin.sourceSets.getByName(name) {
            kotlin.srcDir("build/generated/ksp/$name/kotlin")
        }
    }

    kapt {
        correctErrorTypes = true
    }

}

dependencies {
    // Firebase (Kotlin 2.1.0 호환 최신 BOM)
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // AndroidX & Compose (Kotlin 2.1.0 호환 버전)
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Jetpack 등 기타
    implementation("androidx.room:room-common:2.7.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.browser:browser:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.fragment:fragment-ktx:1.8.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0")
    implementation("com.google.android.material:material:1.12.0")

    // Network
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-okhttp:2.3.8")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")


    // Hilt
    implementation("com.google.dagger:hilt-android:2.55")
    kapt("com.google.dagger:hilt-compiler:2.55")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.0")

    //JSON을 KOtlin데이터 객체로 변환
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")


    //add("kotlinCompilerPluginClasspath", "androidx.compose.compiler:compiler:1.5.13")

    // Navigation Component를 사용하려면 다음도 추가
    // Coil for image loading
    implementation("io.coil-kt:coil:2.6.0") // 최신 버전 확인

    // Coil's SVG Decoder (AndroidSVG를 내부적으로 사용하거나 유사한 기능 제공)
    implementation("io.coil-kt:coil-svg:2.6.0") // coil과 동일한 버전 사용
    // Retrofit for networking
    // OkHttp (Retrofit이 내부적으로 사용, 로깅 인터셉터 등에 필요할 수 있음)
    //implementation("com.squareup.okhttp3:okhttp:4.10.0") // 최신 버전 확인
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0") // (선택 사항: 디버깅용)

    // Coroutines for asynchronous operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // 최신 버전 확인

    implementation("com.airbnb.android:lottie:5.0.2")


}