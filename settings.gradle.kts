rootProject.name = "segovia"

include("app")
project(":app").name = "segovia-app"

include("core")
project(":core").name = "segovia-core"

include("ui")
project(":ui").name = "segovia-ui"

include("web")
project(":web").name = "segovia-web"
