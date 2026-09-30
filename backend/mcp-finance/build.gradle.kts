plugins {
    java
    id("org.springframework.boot")
}

// MCP server: read-only finance tools (§17.3, §18.2, §24).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
}
