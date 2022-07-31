package dev.helight.kommando.command

import com.google.inject.*
import dev.helight.kommando.CommandContext
import dev.helight.kommando.CommandResult
import dev.helight.kommando.CommandScope

interface CommandDefinition {

    fun getSortIndex(): Int

    fun accepts(context: CommandContext, scope: CommandScope): Boolean
    suspend fun invoke(context: CommandContext, scope: CommandScope, injector: Injector): CommandResult

    fun getModules(): Collection<Module>

}