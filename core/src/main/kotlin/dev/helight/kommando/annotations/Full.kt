package dev.helight.kommando.annotations

import dev.helight.kommando.arguments.AnnotationMapper

/**
 * Modifies a string argument to read all remaining arguments instead of just a single word
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Full

class FullTextMod

class FullMapper: AnnotationMapper {
    override fun accepts(annotation: Any): Boolean {
        return annotation is Full
    }

    override fun resolve(annotation: Any): Any {
        return FullTextMod()
    }
}