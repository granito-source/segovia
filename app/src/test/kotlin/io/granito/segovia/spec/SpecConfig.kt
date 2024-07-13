package io.granito.segovia.spec

import io.granito.segovia.app.Application
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.logging.LoggingPreferences
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import java.util.logging.Level

@Configuration
@ComponentScan
@Import(Application::class)
@Lazy
open class SpecConfig {
    @Bean
    @Qualifier("browser")
    @Description("Browser window size")
    open fun browserWindowSize() = Dimension(1600, 1200)

    @Bean(destroyMethod = "quit")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Description("Web Driver for Selenium tests")
    open fun webDriver(@Qualifier("browser") size: Dimension): WebDriver {
        val options = ChromeOptions()
        val logging = LoggingPreferences()

        logging.enable(LogType.BROWSER, Level.ALL)
        options.setCapability(ChromeOptions.LOGGING_PREFS, logging)
        options.addArguments("no-sandbox")
        options.addArguments("headless")
        options.addArguments("window-size=${size.width}x${size.height}")
        options.addArguments("disable-gpu")
        options.addArguments("disable-features=VizDisplayCompositor")
        options.addArguments("remote-allow-origins=*")

        return ChromeDriver(ChromeDriverService.createDefaultService(),
            options)
    }
}
