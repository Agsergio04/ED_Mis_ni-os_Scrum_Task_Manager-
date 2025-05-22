plugins {
    kotlin("jvm") version "2.0.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

detekt {
    toolVersion = "1.23.1" // Coincide con la versión del plugin
    config = files("detekt.yml") // Archivo de configuración personalizado (crearemos este archivo)
    buildUponDefaultConfig = true // Parte de la configuración predeterminada
    reports {
        html.enabled = true // Reporte en HTML
        xml.enabled = false
        txt.enabled = false
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    implementation ("org.slf4j:slf4j-api:1.7.32")
    implementation ("ch.qos.logback:logback-classic:1.2.6")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"}}