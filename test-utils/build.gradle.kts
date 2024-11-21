import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.psycodeinteractive.weathertracker.testutils"
    compileSdk = libs.versions.appCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.appMinSdk.get().toInt()
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
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    )
                )
            }
        }
    }
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.presentation)

    api(libs.test.junit)
    api(libs.test.junit.kotlin)
    api(libs.test.coroutines)
    api(libs.test.android)
    api(libs.test.android.ktx)
}