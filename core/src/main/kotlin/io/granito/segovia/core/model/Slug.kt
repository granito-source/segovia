package io.granito.segovia.core.model

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

data class Slug(private val id: String): Comparable<Slug> {
    companion object {
        private val digest = MessageDigest.getInstance("SHA-1")

        private val encoder = Base64.getUrlEncoder().withoutPadding()

        @JvmStatic
        fun of(text: String) = Slug(generate(text))

        private fun generate(text: String) = encoder
            .encodeToString(digest.digest(text.toByteArray(
                StandardCharsets.UTF_8)))
            .substring(0, 12)
    }

    override fun compareTo(other: Slug) = id.compareTo(other.id)

    override fun toString() = id
}
