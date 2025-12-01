plugins {
    kotlin("jvm") version "2.2.20"
    id("io.kotest") version "6.0.7"
}

group = "advent-of-code-2025"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("test"))

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-framework-engine:6.0.7")
    testImplementation("io.kotest:kotest-assertions-core:6.0.7")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}