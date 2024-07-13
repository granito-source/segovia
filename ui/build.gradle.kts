plugins {
    kotlin("jvm")
}

tasks {
    register<Exec>("npmVersion") {
        description = "Update NPM project version."
        inputs.property("npmVersion", version)
        outputs.file("package.json")
        commandLine("npm", "version", "--no-git-tag-version", "--allow-same-version", version)
    }

    register<Exec>("npmInstall") {
        dependsOn("npmVersion")
        description = "Install NPM dependencies."
        inputs.file("package.json")
        inputs.file("package-lock.json")
        outputs.file("node_modules/.package-lock.json")
        commandLine("npm", "ci")
    }

    register<Exec>("npmBuild") {
        dependsOn("npmInstall")
        description = "Build NPM project."
        inputs.files(fileTree("src/main/typescript") {
            exclude("**/*.spec.*")
        })
        outputs.dir("build/resources/main/META-INF/resources")
        commandLine("npm", "run", "build")
    }

    register<Exec>("npmTest") {
        dependsOn("npmInstall")
        description = "Run NPM project tests."
        commandLine("npm", "test", "--", "--ci")
    }

    jar {
        dependsOn("npmBuild")
    }

    check {
        dependsOn("npmTest")
    }

    clean {
        delete(".angular", "node_modules")
    }
}

kotlin {
    jvmToolchain(21)
}
