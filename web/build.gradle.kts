plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":segovia-core"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.hateoas:spring-hateoas")

    runtimeOnly(project(":segovia-ui"))

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-nop")
}

configurations.implementation {
    exclude(group = "ch.qos.logback", module = "logback-classic")
}

tasks {
    processResources {
        expand(project.properties)
    }
    test {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }
}

kotlin {
    jvmToolchain(21)
}
