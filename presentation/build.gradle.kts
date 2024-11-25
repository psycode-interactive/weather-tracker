import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.psycodeinteractive.weathertracker.presentation"
    compileSdk = libs.versions.appCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.appMinSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
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
        jvmTarget = libs.versions.jvmTarget.get()
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                freeCompilerArgs.addAll(
                    listOf(
                        "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                        "-opt-in=androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi",
                        "-opt-in=kotlinx.coroutines.FlowPreview"
                    )
                )
            }
        }
    }
}

dependencies {
    api(projects.domain)

    api(platform(libs.compose.bom))
    androidTestApi(platform(libs.compose.bom))
    api(libs.compose.navigation)

    api(libs.compose.foundation)
    api(libs.compose.material3)
    api(libs.compose.ui.tooling)

    implementation(libs.coil.compose)
    implementation(libs.coil.okhttp)

    implementation(libs.kotlin.serialization.json)

    implementation(libs.hilt)
    implementation(libs.hilt.compose.navigation)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.compiler.android)

    testImplementation(projects.testUtils)
}
