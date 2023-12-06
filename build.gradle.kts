plugins {
    id("base")
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21" apply false
    id("org.springframework.boot") version "3.2.0" apply false
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
}

allprojects {
    group = "io.granito.segovia"

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }
}
