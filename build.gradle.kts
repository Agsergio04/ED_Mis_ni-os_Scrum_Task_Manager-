plugins {
    kotlin("jvm") version "1.8.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jetbrains.dokka") version "1.8.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}


tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}
