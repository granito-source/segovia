package io.granito.segovia.spec.ui

import io.granito.segovia.spec.SpecBase
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.time.Duration

abstract class UiBase: SpecBase() {
    @LocalServerPort
    private var port = -1;

    @Autowired
    private lateinit var webDriver: WebDriver

    val uri: String
        get() {
            val u = URI.create(webDriver.currentUrl)
            val r = u.path
            val q = u.query

            return if (q != null) "$r?$q" else r
        }

    val title: String
        get() = webDriver.title

    fun load(uri: String) {
        webDriver.get("http://localhost:${port}${uri}")
        waitToLoad()
    }

    fun text(element: WebElement?) = element?.text

    protected fun find(css: String) = find(webDriver, css)

    private fun find(container: SearchContext, css: String) =
        try {
            findAll(container, css).first()
        } catch (ex: NoSuchElementException) {
            null
        }

    private fun findAll(container: SearchContext, css: String) =
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
