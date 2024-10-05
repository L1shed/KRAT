val ktorVersion: String by project

plugins {
    kotlin("jvm") version "2.0.10"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("me.jakejmattson:DiscordKt:0.24.0")
    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}