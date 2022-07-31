package dev.helight.kommando

interface MessageFormatter {

    val priority: Int

    fun accepts(value: Any): Boolean
    fun format(value: Any): String

}

