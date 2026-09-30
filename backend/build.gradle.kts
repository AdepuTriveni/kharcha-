plugins {
    alias(libs.plugins.spring.boot) apply false
}

// Shared setup for every module. Modules still declare their own `plugins {}` block
// (java / java-library / org.springframework.boot) so their scripts get type-safe accessors.
subprojects {
    apply(plugin = "java")

    group = "com.kharcha"
    version = "0.1.0-SNAPSHOT"

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-parameters", "-Xlint:unchecked,deprecation"))
    }

    dependencies {
        "implementation"(platform(libs.spring.boot.bom))
        "implementation"(platform(libs.spring.ai.bom))
        "testImplementation"(libs.junit.jupiter)
        "testImplementation"(libs.assertj)
        "testRuntimeOnly"(libs.junit.platform.launcher)
    }

    // Unit tests: `./gradlew build`. Integration tests (*IT, Testcontainers): `./gradlew integrationTest`.
    // Tests tagged "ollama" need a local Ollama and run only via `:processor:ollamaSmoke`.
    tasks.named<Test>("test") {
        useJUnitPlatform { excludeTags("ollama") }
        exclude("**/*IT.class")
    }

    val sourceSets = extensions.getByType<SourceSetContainer>()
    tasks.register<Test>("integrationTest") {
        description = "Runs *IT integration tests (needs Docker for Testcontainers)."
        group = "verification"
        testClassesDirs = sourceSets["test"].output.classesDirs
        classpath = sourceSets["test"].runtimeClasspath
        useJUnitPlatform { excludeTags("ollama") }
        include("**/*IT.class")
        shouldRunAfter("test")
    }

    tasks.withType<Test>().configureEach {
        // Test tasks are split by filters (unit / *IT / ollama), so a module may legitimately
        // have no tests for one of them; Gradle 9 would otherwise fail that task.
        failOnNoDiscoveredTests = false
        testLogging {
            events("failed", "skipped")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
