package dev.helight.kommando.command

import com.google.inject.Injector
import com.google.inject.Module
import dev.helight.kommando.*
import dev.helight.kommando.arguments.ArgumentContext
import java.util.*

abstract class KommandoBaseCommand(val path: List<String>, val scopeType: Class<*>): CommandDefinition {

    abstract val argumentContexts: List<ArgumentContext>
    abstract val syntaxDescription: String

    override fun accepts(context: CommandContext, scope: CommandScope): Boolean {
        if (context.fragments.size < path.size) return false
        if (!path.mapIndexed { index, string -> string == context.fragments[index] }.all { it }) return false
        if (!scopeType.isAssignableFrom(scope::class.java)) return false
        return true
    }

    override suspend fun invoke(context: CommandContext, scope: CommandScope, injector: Injector): CommandResult {
        val commandArgs = CommandArgs(context.fragments.drop(path.size).asReversed())
        val argsStack = Stack<String>().apply { addAll(commandArgs) }
        context.args = commandArgs
        val deserializedArguments = try {
            argumentContexts.map { it.serializer.deserialize(this, context, scope, injector, it, argsStack) }
        } catch (e: Exception) {
            println("Syntax Error: ${e.message}")
            return CommandResult(syntaxDescription, CommandResultState.BadSyntax)
        }
        val manager = injector.getInstance(KommandoManager::class.java)
        val result = try {
            call(deserializedArguments, injector) ?: return CommandResult("", CommandResultState.NoContent)
        } catch (e: Exception) {
            e.printStackTrace()
            return CommandResult("Oh oh! Something went wrong. (Internal Error)", CommandResultState.Error)
        }
        val responseContent = manager.findFormatter(result).format(result)
        return CommandResult(responseContent, CommandResultState.Success)
    }

    abstract suspend fun call(arguments: List<Any?>, injector: Injector): Any?

    override fun getModules(): Collection<Module> {
        return listOf()
    }

    override fun getSortIndex(): Int {
        return path.size
    }
}