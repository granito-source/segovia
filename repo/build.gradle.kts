plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":segovia-core"))
    implementation("io.projectreactor:reactor-core")
    implementation("org.springframework:spring-context")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.slf4j:slf4j-nop")
}

configurations.testRuntimeOnly {
    exclude(group = "ch.qos.logback", module = "logback-classic")
}

tasks {
    test {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }
}

kotlin {
    jvmToolchain(21)
}
