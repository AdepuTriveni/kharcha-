pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // Downloads JDK 21 automatically if the machine does not have it.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "kharcha-backend"

include(
    // Libraries
    "common",
    "agent-runtime",
    // Deployable Spring Boot apps (§4.1, ADR-001)
    "ingest-api",
    "processor",
    "insights",
    "agents",
    "mcp-finance",
    "mcp-memory",
    "mcp-notify",
    "mcp-refund",
    "notifier",
    "refund-worker",
)
