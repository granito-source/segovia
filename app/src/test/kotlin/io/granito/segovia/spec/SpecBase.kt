package io.granito.segovia.spec

import io.granito.segovia.core.repo.SentenceRepo
import io.granito.segovia.core.usecase.CreateSentenceCase
import org.concordion.api.AfterExample
import org.concordion.api.FullOGNL
import org.concordion.api.option.ConcordionOptions
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles

@RunWith(SpecRunner::class)
@SpringBootTest(classes = [SpecConfig::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@ConcordionOptions(declareNamespaces = ["cx", "urn:concordion-extensions:2010"])
@FullOGNL
abstract class SpecBase {
    @Autowired
    private lateinit var createSentenceCase: CreateSentenceCase

    @Autowired
    private lateinit var sentenceRepo: SentenceRepo

    @AfterExample
    fun baseCleanUp() {
        sentenceRepo.clear().block()
    }

    fun store(text: String) {
        createSentenceCase.create(text).block()
    }

    fun contains(string: String?, sub: String?) =
        if (string != null && sub != null && string.contains(sub))
            sub
        else
            string
}
