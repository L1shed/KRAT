plugins {
    kotlin("jvm") version "2.0.10" // Dual platform
    kotlin("plugin.serialization") version "2.0.10"
//    kotlin("multiplatform") version "2.0.10"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

