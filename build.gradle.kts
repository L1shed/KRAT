plugins {
    kotlin("jvm") version "2.0.10"
}

group = "me.lished"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.java.dev.jna:jna-platform:5.15.0")
    implementation("org.json:json:20240303")
    implementation("com.github.sarxos:webcam-capture:0.3.12")
    implementation("dev.kord:kord-core:0.14.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}