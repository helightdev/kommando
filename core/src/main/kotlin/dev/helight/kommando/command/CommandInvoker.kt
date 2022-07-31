package dev.helight.kommando.command

import com.google.inject.Binder
import com.google.inject.Module
import dev.helight.kommando.CommandContext
import dev.helight.kommando.CommandResult
import dev.helight.kommando.CommandScope
import dev.helight.kommando.KommandoManager

object CommandInvoker {

    suspend fun invoke(manager: KommandoManager, definition: CommandDefinition, context: CommandContext, scope: CommandScope): CommandResult {
        val injector = manager.injector.createChildInjector(mutableListOf<Module>().apply {
            add(DefaultModule(definition, context, scope))
            addAll(manager.modules)
            addAll(definition.getModules())
            addAll(context.getModules())
            addAll(scope.getModules())
        })
        return definition.invoke(context, scope, injector)
    }

    class DefaultModule(val definition: CommandDefinition, val context: CommandContext, val scope: CommandScope): Module {
        override fun configure(binder: Binder) {
            binder.bind(CommandDefinition::class.java).toProvider { definition }
            binder.bind(CommandContext::class.java).toProvider { context }
            binder.bind(CommandScope::class.java).toProvider { scope }

            binder.bind(definition.javaClass).toProvider { definition }
            binder.bind(context.javaClass).toProvider { context }
            binder.bind(scope.javaClass).toProvider { scope }
        }

    }

}