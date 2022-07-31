package dev.helight.kommando.arguments.resolvers

import com.google.inject.Injector
import dev.helight.kommando.CommandContext
import dev.helight.kommando.CommandScope
import dev.helight.kommando.arguments.AnnotationMapper
import dev.helight.kommando.arguments.ArgumentContext
import dev.helight.kommando.arguments.ArgumentResolver
import dev.helight.kommando.arguments.Color
import dev.helight.kommando.command.CommandDefinition
import java.util.*

class ColorResolver: ArgumentResolver {
    override val priority: Int = 0
    override val isContextOnly: Boolean = false

    override fun associatedAnnotation(): Collection<Class<*>> = listOf()
    override fun associatedTypes(): Collection<Class<*>> = listOf(Color::class.java)
    override fun annotationMappers(): Collection<AnnotationMapper> = listOf()

    override fun deserialize(
        definition: CommandDefinition,
        context: CommandContext,
        scope: CommandScope,
        injector: Injector,
        argumentContext: ArgumentContext,
        args: Stack<String>
    ): Any {
        val query = args.pop()
        val hex = query.removePrefix("#").uppercase()
        if (!hex.matches("[\\dA-F]+".toRegex())) error("Invalid hex code")
        if (hex.length != 6) error("Invalid hex length")
        return Color(hex)
    }
}