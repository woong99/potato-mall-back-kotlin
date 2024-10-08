object Versions {
    const val P6SPY_VERSION = "1.9.2"
    const val KOTLIN_LOGGING_VERSION = "7.0.0"
    const val MOCKK_VERSION = "1.13.12"
    const val KOTEST_VERSION = "5.9.1"
}

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
    id("java")
}

group = "com.potatowoong"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    configureEach {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Log4j2
    implementation("org.springframework.boot:spring-boot-starter-log4j2")

    // P6Spy
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:${Versions.P6SPY_VERSION}")

    // Kotlin-Logging
    implementation("io.github.oshai:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING_VERSION}")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // MariaDB
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    // Devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:${Versions.MOCKK_VERSION}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:${Versions.KOTEST_VERSION}")
    testImplementation("io.kotest:kotest-assertions-core-jvm:${Versions.KOTEST_VERSION}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets {
    main {
        java {
            srcDirs("src/main/kotlin")
        }
    }
    test {
        java {
            srcDirs("src/test/kotlin")
        }
    }
}

