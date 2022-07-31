package dev.helight.kommando

import dev.helight.kommando.command.CommandDefinition

fun KommandoManager.findCommand(context: CommandContext, scope: CommandScope): CommandDefinition? = commands.sortedByDescending {
    it.getSortIndex() // Check most nested paths first so the parents can be used as "Default Handlers"
}.firstOrNull { it.accepts(context, scope) }

fun KommandoManager.findFormatter(value: Any): MessageFormatter = formatters.sortedByDescending {
    it.priority
}.firstOrNull { it.accepts(value) } ?: ToStringFormatter()