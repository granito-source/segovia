package io.granito.segovia.web

import io.granito.segovia.core.repo.SentenceRepo
import io.granito.segovia.core.service.SentenceService
import io.granito.segovia.core.service.StatusService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.Lazy

@Configuration
@ImportResource("classpath*:context.xml")
@Lazy
open class CoreConfig {
    @Bean
    open fun statusService(@Qualifier("app.version") version: String) =
        StatusService(version)

    @Bean
    open fun sentenceService(sentenceRepo: SentenceRepo) =
        SentenceService(sentenceRepo)
}
