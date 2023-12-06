package io.granito.segovia.spec

import org.concordion.api.FullOGNL
import org.concordion.api.option.ConcordionOptions
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles

@RunWith(SpecRunner::class)
@SpringBootTest(classes = [SpecConfig::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@ConcordionOptions(declareNamespaces = ["ext", "urn:concordion-extensions:2010"])
@FullOGNL
abstract class SpecBase {
    fun contains(string: String?, sub: String?): String? {
        return if (string != null && sub != null && string.contains(sub))
            sub
        else
            string
    }
}
