package io.granito.segovia.core.model

class Sentence(val id: Slug, val text: String) {
    constructor(text: String): this(Slug.of(text), text)
}
