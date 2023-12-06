plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.hateoas:spring-hateoas")
    runtimeOnly(project(":segovia-ui"))
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(17)
}
