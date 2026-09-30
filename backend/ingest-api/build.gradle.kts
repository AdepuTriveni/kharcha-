plugins {
    java
    id("org.springframework.boot")
}

// REST API: phone uploads, Ask, bill photos, settings (§9).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.flyway)
    implementation(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
}
