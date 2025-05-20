plugins {
    kotlin("jvm") version "2.0.21"
    id("io.kotest") version "0.3.9"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"

}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(false)
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")

}

tasks.test {
    useJUnitPlatform()
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
    useJUnitPlatform()
}

detekt {
    toolVersion = "1.23.6"
    config.setFrom(files("config/detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(false)
    }
}

kotlin {
    jvmToolchain(21)
}
