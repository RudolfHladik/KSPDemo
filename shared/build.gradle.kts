plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
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
            // add generated sources as project sources
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
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
        val androidMain by getting {
            dependsOn(commonMain)
        }
        val iosMain by getting {
            dependsOn(commonMain)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", project(":property-processor"))
//    add("kspAndroid", project(":property-processor"))
//    add("kspIosX64", project(":property-processor"))
//    add("kspIosArm64", project(":property-processor"))
//    add("kspIosSimulatorArm64", project(":property-processor"))

//     The universal "ksp" configuration has performance issue and is deprecated on multiplatform since 1.0.1
//     ksp(project(":test-processor"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>().all {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}


android {
    namespace = "com.rudolfhladik.kspdemo"
    compileSdk = 33
    defaultConfig {
        minSdk = 28
    }
}
