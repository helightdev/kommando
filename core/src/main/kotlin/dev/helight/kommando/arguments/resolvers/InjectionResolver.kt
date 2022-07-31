package dev.helight.kommando.arguments.resolvers

import com.google.inject.Injector
import dev.helight.kommando.*
import dev.helight.kommando.arguments.AnnotationMapper
import dev.helight.kommando.arguments.ArgumentResolver
import dev.helight.kommando.arguments.ArgumentContext
import dev.helight.kommando.command.CommandDefinition
import java.util.*

class InjectionResolver: ArgumentResolver {
    override val priority: Int = -200
    override val isContextOnly: Boolean = true

    override fun associatedAnnotation(): Collection<Class<*>> = listOf()

    override fun associatedTypes(): Collection<Class<*>> = listOf(Any::class.java)

    override fun annotationMappers(): Collection<AnnotationMapper> = listOf()

    override fun deserialize(
        definition: CommandDefinition,
        context: CommandContext,
        scope: CommandScope,
        injector: Injector,
        argumentContext: ArgumentContext,
        args: Stack<String>
    ): Any? {
        return injector.tryGetInstanceUnsafe(argumentContext.type)
    }
}