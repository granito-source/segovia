package io.granito.segovia.web.model

import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponseException
import java.net.URI

class SentenceNotFoundException(id: String):
    ErrorResponseException(HttpStatus.NOT_FOUND) {
    init {
        setType(URI("https://segovia.granito.io/problem/not-found/sentence"))
        setTitle("Sentence is not found.")
        setDetail("Sentence identified by '$id' is not found.")
    }
}
