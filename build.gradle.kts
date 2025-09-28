plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
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
