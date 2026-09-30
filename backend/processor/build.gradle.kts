plugins {
    java
    id("org.springframework.boot")
}

// Parsing (rules → own model → teacher LLM), dedup, merchants, cash (§10–§13).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
    implementation(libs.spring.ai.starter.ollama)
}

// Needs Ollama running on the host with the model pulled (see README):
//   ./gradlew :processor:ollamaSmoke
tasks.register<Test>("ollamaSmoke") {
    description = "Calls a local Ollama model through Spring AI."
    group = "verification"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform { includeTags("ollama") }
}
