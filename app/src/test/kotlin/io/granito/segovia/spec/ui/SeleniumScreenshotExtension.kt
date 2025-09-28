package io.granito.segovia.spec.ui

import java.awt.Dimension
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.OutputStream
import javax.imageio.ImageIO.getImageReadersBySuffix
import javax.imageio.stream.MemoryCacheImageInputStream
import org.concordion.ext.ScreenshotExtension
import org.concordion.ext.ScreenshotTaker
import org.concordion.ext.ScreenshotUnavailableException
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver

class SeleniumScreenshotExtension : ScreenshotExtension(), ScreenshotTaker {
    private lateinit var webDriver: WebDriver

    init {
        setScreenshotTaker(this)
    }

    fun setWebDriver(webDriver: WebDriver) {
        this.webDriver = webDriver
    }

    override fun writeScreenshotTo(outputStream: OutputStream): Dimension {
        val st = webDriver

        outputStream.use {
            if (st !is TakesScreenshot)
                throw ScreenshotUnavailableException(
                    "WebDriver does not support screenshot taking")

            val bytes = st.getScreenshotAs(OutputType.BYTES)

            it.write(bytes)

            return extractDimensions(bytes)
        }
    }

    override fun getFileExtension() = "png"

    private fun extractDimensions(bytes: ByteArray): Dimension {
        val ext = fileExtension
        val readers = getImageReadersBySuffix(ext)

        while (readers.hasNext()) {
            val reader = readers.next()

            MemoryCacheImageInputStream(ByteArrayInputStream(bytes)).use {
                reader.input = it

                try {
                    return Dimension(reader.getWidth(reader.minIndex),
                        reader.getHeight(reader.minIndex))
                } catch (_: IOException) {
                    // ignore failed attempt
                } finally {
                    reader.dispose()
                }
            }
        }

        throw IOException("unable to read .$ext image")
    }
}
