package dev.helight.kommando.arguments

interface AnnotationMapper {

    fun accepts(annotation: Any): Boolean
    fun resolve(annotation: Any): Any?

}