package dev.helight.kommando.arguments.resolvers

import com.google.inject.Injector
import dev.helight.kommando.*
import dev.helight.kommando.annotations.FullMapper
import dev.helight.kommando.annotations.FullTextMod
import dev.helight.kommando.arguments.AnnotationMapper
import dev.helight.kommando.arguments.ArgumentResolver
import dev.helight.kommando.arguments.ArgumentContext
import dev.helight.kommando.command.CommandDefinition
import java.util.*

class StringResolver: ArgumentResolver {
    override val priority: Int = -100
    override val isContextOnly: Boolean = false

    override fun associatedAnnotation(): Collection<Class<*>> = listOf()
    override fun associatedTypes(): Collection<Class<*>> = listOf(String::class.java)
    override fun annotationMappers(): Collection<AnnotationMapper> = listOf(FullMapper())

    override fun deserialize(
        definition: CommandDefinition,
        context: CommandContext,
        scope: CommandScope,
        injector: Injector,
        argumentContext: ArgumentContext,
        args: Stack<String>
    ): Any? {
        if (argumentContext.has<FullTextMod>()) {
            return args.indices.joinToString(" ") { args.pop() }
        }
        return args.pop()
    }
}