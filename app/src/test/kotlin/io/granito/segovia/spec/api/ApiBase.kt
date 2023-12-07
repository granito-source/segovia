package io.granito.segovia.spec.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.granito.segovia.spec.SpecBase
import org.concordion.api.MultiValueResult
import org.concordion.api.MultiValueResult.multiValueResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.StandardCharsets

abstract class ApiBase: SpecBase() {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var webClient: WebTestClient

    fun prettyPrint(json: ByteArray): String = try {
        objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(objectMapper.readTree(json))
    } catch (_: Exception) {
        json.toString(StandardCharsets.UTF_8)
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
        val body: Any? =
            if (content == null || content.isEmpty())
                null
            else
                if (status >= 400)
                    objectMapper.readValue(content,
                        object: TypeReference<HashMap<String, String>>() {})
                else if (converter != null)
                    converter(content)
                else
                    content
        val headers = result.responseHeaders

        return multiValueResult()
            .with("status", status)
            .with("contentType", headers.contentType?.toString())
            .with("location", headers.location?.toString())
            .with("wwwAuthenticate", headers["WWW-Authenticate"])
            .with("body", body)
    }
}
