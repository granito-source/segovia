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
