plugins {
    kotlin("jvm")
}

//group = "me.lished"
//version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.java.dev.jna:jna-platform:5.15.0")
    implementation("org.json:json:20240303")
    implementation("com.github.sarxos:webcam-capture:0.3.12")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}