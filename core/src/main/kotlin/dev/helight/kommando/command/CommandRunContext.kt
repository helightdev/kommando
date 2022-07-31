package dev.helight.kommando.command

import com.google.inject.Injector
import dev.helight.kommando.CommandContext
import dev.helight.kommando.CommandScope
import dev.helight.kommando.tryGetInstance

class CommandRunContext(
    val injector: Injector
) {

    val context: CommandContext
        get() = injector.getInstance(CommandContext::class.java)

    val scope: CommandScope
        get() = injector.getInstance(CommandScope::class.java)

    val definition: CommandDefinition
        get() = injector.getInstance(CommandDefinition::class.java)

    inline fun <reified T> get(): T {
        return injector.getInstance(T::class.java)
    }

    inline fun <reified T> tryGet(): T? {
        return injector.tryGetInstance(T::class.java)
    }
}