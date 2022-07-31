package dev.helight.kommando

class ToStringFormatter: MessageFormatter {
    override val priority: Int = -100

    override fun accepts(value: Any): Boolean = true

    override fun format(value: Any): String {
        return value.toString()
    }
}