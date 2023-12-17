package io.granito.segovia.spec.api

import io.granito.segovia.spec.SpecBase
import org.concordion.api.MultiValueResult
import org.concordion.api.MultiValueResult.multiValueResult
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

abstract class ApiBase: SpecBase() {
    @Autowired
    private lateinit var webClient: WebTestClient

    fun containsJson(json: ByteArray?, expected: String?): String? {
        val actual = json?.toString(Charsets.UTF_8)

        return if (JSONCompare.compareJSON(expected, actual,
            JSONCompareMode.LENIENT).passed())
            expected
        else
            actual
    }

    fun http(method: String, uri: String) =
        http<ByteArray>(method, uri, null, null)

    private fun <T> http(method: String, uri: String, input: Any?,
        converter: ((ByteArray) -> T)?): MultiValueResult {
        val init = webClient
            .method(HttpMethod.valueOf(method) ?: HttpMethod.GET)
            .uri(uri)
        val ready =
            if (input != null)
                init
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(input)
            else
                init
        val result = ready
            .exchange()
            .returnResult(ByteArray::class.java)
        val status = result.status.value()
        val content = result.responseBodyContent
        val headers = result.responseHeaders

        return multiValueResult()
            .with("status", status)
            .with("contentType", headers.contentType?.toString())
            .with("location", headers.location?.toString())
            .with("wwwAuthenticate", headers["WWW-Authenticate"])
            .with("body", when {
                content == null || content.isEmpty() -> null
                converter != null -> converter(content)
                else -> content
            })
    }
}
