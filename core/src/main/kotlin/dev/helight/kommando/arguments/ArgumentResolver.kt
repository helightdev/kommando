package dev.helight.kommando.arguments

import com.google.inject.Injector
import dev.helight.kommando.CommandContext
import dev.helight.kommando.command.CommandDefinition
import dev.helight.kommando.CommandScope
import java.lang.reflect.Parameter
import java.util.Stack
import kotlin.reflect.KParameter

interface ArgumentResolver {

    val priority: Int
    val isContextOnly: Boolean

    fun associatedAnnotation(): Collection<Class<*>>
    fun associatedTypes(): Collection<Class<*>>
    fun annotationMappers(): Collection<AnnotationMapper>

    fun deserialize(definition: CommandDefinition, context: CommandContext, scope: CommandScope, injector: Injector, argumentContext: ArgumentContext, args: Stack<String>): Any?

    fun checkParameter(parameter: Parameter): Boolean {
        if (associatedTypes().any { it.isAssignableFrom(parameter.type) }) return true
        if (associatedAnnotation().any { parameter.annotations.any { target -> it.isAssignableFrom(target.annotationClass.java) } }) return true
        return false
    }

    fun checkParameter(parameter: KParameter): Boolean {
        if (associatedTypes().any { it.isAssignableFrom(parameter.type.javaClass) }) return true
        if (associatedAnnotation().any { parameter.annotations.any { target -> it.isAssignableFrom(target.annotationClass.java) } }) return true
        return false
    }

}

