plugins {
    java
    id("org.springframework.boot")
}

// Forecast, risk scoring (ONNX), bandit, triggers (§14, §19, §22.1).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
}
