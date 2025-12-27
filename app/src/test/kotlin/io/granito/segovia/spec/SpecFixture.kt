package io.granito.segovia.spec

import io.granito.concordion.spring.ConcordionFixture
import org.concordion.api.ConcordionResources

@ConcordionFixture
@ConcordionResources("/index.html")
class SpecFixture: SpecBase()
