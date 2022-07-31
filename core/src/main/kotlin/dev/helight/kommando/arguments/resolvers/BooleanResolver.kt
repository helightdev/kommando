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

class BooleanResolver: ArgumentResolver {
    override val priority: Int = 0
    override val isContextOnly: Boolean = false

    override fun associatedAnnotation(): Collection<Class<*>> = listOf()
    override fun associatedTypes(): Collection<Class<*>> = listOf(Boolean::class.java)
    override fun annotationMappers(): Collection<AnnotationMapper> = listOf()

    override fun deserialize(
        definition: CommandDefinition,
        context: CommandContext,
        scope: CommandScope,
        injector: Injector,
        argumentContext: ArgumentContext,
        args: Stack<String>
    ): Any? {
        val query = args.pop()
        val bool: Boolean? = when(query.lowercase()) {
            in trueAnswers -> true
            in falseAnswers -> false
            else -> null
        }
        return bool
    }

    companion object {
        // This should cover all common answer for yes/no
        // Note: Should I add some easter-egg for "maybe"?
        private val trueAnswers: Set<String> = setOf("true", "yes", "y", "1", "+", "yea", "yeah", "aye", "aye!")
        private val falseAnswers: Set<String> = setOf("false", "no", "n", "0", "-", "nay", "nay!")
    }
}