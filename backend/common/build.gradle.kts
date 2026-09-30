plugins {
    `java-library`
}

// Pure Java: no Spring here, so every module (and its tests) can use it cheaply.
// Also ships the Flyway migrations (src/main/resources/db/migration) shared by all services.

tasks.test {
    // TopicsTest checks the topic init script against Topics.ALL.
    inputs.file(rootProject.file("../infra/kafka/create-topics.sh"))
}
