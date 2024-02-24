package io.granito.segovia.core.model

class Sentence(val lang: Lang, val id: Slug, val text: String) {
    constructor(lang: Lang, text: String): this(lang, Slug.of(text), text)
}
