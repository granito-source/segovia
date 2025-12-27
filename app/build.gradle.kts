plugins {
    kotlin("jvm")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":segovia-core"))
    implementation(project(":segovia-repo"))
    implementation(project(":segovia-web"))
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-webtestclient")
    testImplementation("io.granito.concordion:concordion-spring:1.0.3")
    testImplementation("io.granito.concordion:concordion-ext-screenshot:1.0.3")
    testImplementation("org.seleniumhq.selenium:selenium-java")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    processTestResources {
        from("src/test/doc")
        expand(project.properties)
    }

    test {
        useJUnitPlatform()
        include("**/SpecFixture.class")
        testLogging.showStandardStreams = true
        systemProperties["concordion.output.dir"] = "build/reports/spec"
        outputs.upToDateWhen { false }
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }

    bootJar {
        mainClass.set("io.granito.segovia.app.ApplicationKt")
        manifest {
            attributes("Main-Class" to "org.springframework.boot.loader.launch.PropertiesLauncher")
        }
    }
}

kotlin {
    jvmToolchain(25)
}
