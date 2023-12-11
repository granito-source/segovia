plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.hateoas:spring-hateoas")
    implementation(project(":segovia-core"))
    runtimeOnly(project(":segovia-ui"))
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    processResources {
        expand(project.properties)
    }
    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(21)
}
