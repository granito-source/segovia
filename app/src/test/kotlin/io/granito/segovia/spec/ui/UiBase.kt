package io.granito.segovia.spec.ui

import java.net.URI
import java.time.Duration
import io.granito.concordion.ext.SeleniumScreenshotExtension
import io.granito.segovia.spec.SpecBase
import jakarta.annotation.PostConstruct
import org.concordion.api.extension.Extension
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort

abstract class UiBase: SpecBase() {
    @LocalServerPort
    private var port = -1

    @Autowired
    private lateinit var webDriver: WebDriver

    @Extension
    private val screenshot = SeleniumScreenshotExtension()

    val uri: String
        get() {
            val u = URI.create(webDriver.currentUrl)
            val r = u.path
            val q = u.query

            return if (q != null) "$r?$q" else r
        }

    val title: String?
        get() = webDriver.title

    val body
        get() = findByCss("body")

    @PostConstruct
    fun uiBasePostConstruct() {
        screenshot.setMaxWidth(300)
        screenshot.setWebDriver(webDriver)
    }

    fun load(uri: String) {
        webDriver.get("http://localhost:${port}${uri}")
        waitToLoad()
    }

    fun pageHeader(container: SearchContext?): WebElement? {
        return findByTestId(container, "page-header")
    }

    fun text(element: WebElement?) = element?.text

    protected fun findByTestId(container: SearchContext?, id: String) =
        findByCss(container, "[data-testid='$id']")

    private fun findByCss(css: String) = findByCss(webDriver, css)

    private fun findByCss(container: SearchContext?, css: String) =
        findAllByCss(container, css).firstOrNull()

    private fun findAllByCss(container: SearchContext?, css: String) =
        if (container == null)
            emptyList()
        else
            container.findElements(By.cssSelector(css))

    private fun waitToLoad() {
        delay(100)
        angularWait()
    }

    private fun delay(millis: Long) = Thread.sleep(millis)

    private fun angularWait() {
        WebDriverWait(webDriver, Duration.ofSeconds(10))
            .until {
                (it as JavascriptExecutor).executeScript(
                    "return !!window.getAllAngularTestabilities &&" +
                    "  window.getAllAngularTestabilities()" +
                    "    .findIndex(t => !t.isStable()) === -1;"
                ) as Boolean
            }
    }
}
