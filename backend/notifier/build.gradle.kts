plugins {
    java
    id("org.springframework.boot")
}

// Policy gate, Telegram, FCM, feedback capture (§22.2, §25).

dependencies {
    implementation(project(":common"))
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    runtimeOnly(libs.micrometer.prometheus)
    testImplementation(libs.spring.boot.starter.test)
}
