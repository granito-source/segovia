package io.granito.segovia.core.model

fun langFrom(code: String) = Lang.valueOf(code.uppercase())

enum class Lang {
    EN,
    ES,
    FR,
    IT;

    override fun toString() = name.lowercase()
}
