plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.hateoas:spring-hateoas")
    implementation(project(":segovia-core"))
    implementation(project(":segovia-repo"))
    runtimeOnly(project(":segovia-ui"))
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
