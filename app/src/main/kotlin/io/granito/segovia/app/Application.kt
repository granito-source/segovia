package io.granito.segovia.app

import io.granito.segovia.repo.RepoConfig
import io.granito.segovia.web.WebConfig
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Configuration
@Import(CoreConfig::class, RepoConfig::class, WebConfig::class)
open class Application
