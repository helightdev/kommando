package dev.helight.kommando.annotations

import dev.helight.kommando.arguments.AnnotationMapper

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Mention

class MentionMod

class MentionMapper: AnnotationMapper {
    override fun accepts(annotation: Any): Boolean {
        return annotation is Mention
    }

    override fun resolve(annotation: Any): Any {
        return MentionMod()
    }
}