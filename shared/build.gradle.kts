plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.devtools.ksp")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/android/androidDebug/kotlin")
//            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            dependencies {
                //put your multiplatform dependencies here
                implementation(project(":long-property-annotation"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":property-processor"))
    add("kspAndroid", project(":property-processor"))
    add("kspIosX64", project(":property-processor"))
    add("kspIosSimulatorArm64", project(":property-processor"))
    // The universal "ksp" configuration has performance issue and is deprecated on multiplatform since 1.0.1
    // ksp(project(":test-processor"))
}

android {
    namespace = "com.rudolfhladik.kspdemo"
    compileSdk = 33
    defaultConfig {
        minSdk = 28
    }
}