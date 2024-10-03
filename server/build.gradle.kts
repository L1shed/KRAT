plugins {
    kotlin("jvm") version "2.0.10"
}

//group = "me.lished"
//version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
    implementation("me.jakejmattson:DiscordKt:0.24.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}