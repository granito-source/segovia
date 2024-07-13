plugins {
    kotlin("jvm")
}

dependencies {
    implementation("io.projectreactor:reactor-core")
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
    test {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }
}

kotlin {
    jvmToolchain(21)
}
