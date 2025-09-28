pluginManagement {
    plugins {
        id("io.spring.dependency-management") version "1.1.7"
        id("org.springframework.boot") version "3.5.6"
        kotlin("jvm") version "2.1.20"
    }
}

rootProject.name = "segovia"

include("app")
project(":app").name = "segovia-app"

include("core")
project(":core").name = "segovia-core"

include("repo")
project(":repo").name = "segovia-repo"

include("ui")
project(":ui").name = "segovia-ui"

include("web")
project(":web").name = "segovia-web"
