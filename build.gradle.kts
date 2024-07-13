plugins {
    id("base")
    kotlin("jvm") version "1.9.24" apply false
    id("io.spring.dependency-management") version "1.1.6"
    id("org.springframework.boot") version "3.3.1" apply false
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
