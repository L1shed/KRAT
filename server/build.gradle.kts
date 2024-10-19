val ktorVersion: String by project

//plugins {
//    kotlin("jvm") version "2.0.10"
//    kotlin("plugin.serialization") version "2.0.10"
//}

dependencies {
    implementation(project(":shared"))
    testImplementation(kotlin("test"))
    implementation("me.jakejmattson:DiscordKt:0.24.0")
    // Ktor
//    implementation("io.ktor:ktor-server-core:$ktorVersion") !! check versions matching
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")
}

tasks {
    test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(21)
    }

    compileKotlin {
        doLast("writeProperties") {}
    }

    register<WriteProperties>("writeProperties") {
        property("name", project.name)
        property("description", project.description.toString())
        property("version", version.toString())
        property("url", "https://github.com/L1shed/KRAT")
        destinationFile = file("src/main/resources/bot.properties")
    }
}