package dev.helight.kommando.arguments

import java.lang.reflect.Parameter

abstract class ArgumentResolverBase(override val priority: Int = 0, override val isContextOnly: Boolean = false):
    ArgumentResolver {

    val associatedAnnotations: MutableList<Class<*>> = mutableListOf()
    val associatedTypes: MutableList<Class<*>> = mutableListOf()
    val annotationMappers: MutableList<AnnotationMapper> = mutableListOf()

    override fun associatedAnnotation(): Collection<Class<*>> = associatedAnnotations
    override fun associatedTypes(): Collection<Class<*>> = associatedTypes
    override fun annotationMappers(): Collection<AnnotationMapper> = annotationMappers

    override fun checkParameter(parameter: Parameter): Boolean {
        return super.checkParameter(parameter)
    }
}