plugins {
    kotlin("jvm")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":segovia-web"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.concordion:concordion:4.0.1")
    testImplementation("org.seleniumhq.selenium:selenium-chrome-driver")
    testImplementation("org.seleniumhq.selenium:selenium-support")
}

springBoot {
    mainClass.set("io.granito.segovia.web.ApplicationKt")
}

tasks {
    processTestResources {
        from("src/test/doc")
        expand(project.properties)
    }

    test {
        testLogging.showStandardStreams = true
        systemProperties["concordion.output.dir"] = "${reporting.baseDir}/spec"
        include("**/*Fixture.class")
    }
}

kotlin {
    jvmToolchain(17)
}
