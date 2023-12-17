package io.granito.segovia.web

import io.granito.segovia.repo.fake.FakeSentenceRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
@Lazy
open class RepoConfig {
    @Bean
    open fun sentenceRepo() = FakeSentenceRepo()
}
