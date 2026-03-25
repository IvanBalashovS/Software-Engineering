plugins {
    kotlin("jvm") version "1.9.20" // Обновлённая версия Kotlin
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" // Совместимая версия ktlint
}

group = "com.example"
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
    jvmToolchain(17) // Или 21, если хотите использовать вашу Java 21
}

// Опционально: настройки ktlint
ktlint {
    version.set("1.0.1") // Версия самого ktlint
    filter {
        exclude("**/generated/**")
    }
}
