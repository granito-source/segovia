package io.granito.segovia.spec.ui

import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement

class UiFixture: UiBase() {
    fun activeSentence(container: SearchContext?): WebElement? {
        return findByTestId(container, "active-sentence")
    }
}
