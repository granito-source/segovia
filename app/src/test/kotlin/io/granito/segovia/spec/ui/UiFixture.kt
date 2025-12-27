package io.granito.segovia.spec.ui

import io.granito.concordion.spring.ConcordionFixture
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement

@ConcordionFixture
class UiFixture: UiBase() {
    fun activeSentence(container: SearchContext?): WebElement? {
        return findByTestId(container, "active-sentence")
    }
}
