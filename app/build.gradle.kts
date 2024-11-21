import java.time.ZonedDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.psycodeinteractive.weathertracker"
    compileSdk = libs.versions.appCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.psycodeinteractive.weathertracker"
        minSdk = libs.versions.appMinSdk.get().toInt()
        targetSdk = libs.versions.appTargetSdk.get().toInt()
        versionCode = generateVersionCode()
        versionName = generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        jvmTarget = libs.versions.jvmTarget.get()
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("/META-INF/LICENSE*")
            excludes.add("META-INF/gradle/incremental.annotation.processors")
        }
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)

    implementation(libs.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.timber)
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.android)

    debugImplementation(libs.leakcanary)
}

fun generateVersionName(): String {
    return libs.versions.run { "${appVersionMajor.get()}.${appVersionMinor.get()}.${appVersionPatch.get()}" }
}

fun generateVersionCode(): Int {
    val now = ZonedDateTime.now(ZoneOffset.UTC)
    val formattedDate = DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(now)
    return formattedDate.substring(2, formattedDate.lastIndex).toInt()
}
