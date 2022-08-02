package dev.helight.kommando.arguments

data class Color(val hex: String) {

    val decimal: Int
        get() = hex.toInt(16)

    override fun toString(): String {
        return "#$hex"
    }

    companion object {
        fun parse(value: String): Color {
            return Color(value.removePrefix("#"))
        }
    }
}