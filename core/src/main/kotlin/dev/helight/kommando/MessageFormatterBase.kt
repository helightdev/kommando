package dev.helight.kommando

abstract class MessageFormatterBase<T>(val clazz: Class<T>, override val priority: Int): MessageFormatter {

    override fun accepts(value: Any): Boolean {
        return clazz.isAssignableFrom(value::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    override fun format(value: Any): String {
        return call(value as T)
    }

    abstract fun call(obj: T): String
}