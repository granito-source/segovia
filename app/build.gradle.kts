plugins {
    kotlin("jvm")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":segovia-core"))
    implementation(project(":segovia-repo"))
    implementation(project(":segovia-web"))
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation(project(":segovia-core"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.concordion:concordion:4.0.1")
    testImplementation("org.concordion:concordion-screenshot-extension:1.3.0")
    testImplementation("org.seleniumhq.selenium:selenium-java")
    testImplementation("org.junit.vintage:junit-vintage-engine")
}

tasks {
    processTestResources {
        from("src/test/doc")
        expand(project.properties)
    }

    test {
        testLogging.showStandardStreams = true
        systemProperties["concordion.output.dir"] = "${reporting.baseDir}/spec"
        include("**/SpecFixture.class")
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
    jvmToolchain(21)
}
