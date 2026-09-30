plugins {
    java
    id("org.springframework.boot")
}

// Temporal refund workflows (§23).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
}
