pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion = "1.9.23"
        id("com.android.application") version "8.8.2" // 혹은 형님 쓰는 8.8.2
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("com.google.dagger.hilt.android") version "2.51"
        id("com.google.devtools.ksp") version "1.9.23-1.0.20"
        id("com.google.gms.google-services") version "4.4.2"
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "softwareproject"
include(":app")
