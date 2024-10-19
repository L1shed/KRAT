plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
}


allprojects {
    apply { plugin("org.jetbrains.kotlin.jvm") }
    apply { plugin("org.jetbrains.kotlin.plugin.serialization") }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    }

    kotlin {
        jvmToolchain(21)
    }
}