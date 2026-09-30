plugins {
    java
    id("org.springframework.boot")
}

// Orchestrator + Coach, Cash Detective, Refund Advocate, Analyst, Memory Keeper (§17).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
    implementation(project(":agent-runtime"))
}
