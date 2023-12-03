rootProject.name = "segovia"

include("app")
project(":app").name = "segovia-app"

include("web")
project(":web").name = "segovia-web"
