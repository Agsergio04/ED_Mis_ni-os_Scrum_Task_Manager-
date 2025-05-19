plugins {
    kotlin("jvm") version "1.8.0"                  // plugin Kotlin
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0" // plugin ktlint
}

repositories {
    mavenCentral()
}

ktlint {
    version.set("0.49.1")
    outputToConsole.set(true)
    ignoreFailures.set(false)
    android.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



dependencies {
    testImplementation(kotlin("test"))
}


var mockKVersion = "1.13.4"

dependencies {
    testImplementation("io.mockk:mockk:$mockKVersion")
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
    useJUnitPlatform()                         // Usa JUnit 5 / Kotest sobre JUnit
    jvmArgs("-XX:+EnableDynamicAgentLoading")  // Suprime el warning de agentes dinámicos :contentReference[oaicite:0]{index=0}
}



tasks.withType<Test> {
    useJUnitPlatform() // Necesario para Kotest
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}