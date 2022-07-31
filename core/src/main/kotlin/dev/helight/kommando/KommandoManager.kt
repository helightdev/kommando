package dev.helight.kommando

import com.google.inject.*
import dev.helight.kommando.arguments.ArgumentResolver
import dev.helight.kommando.command.CommandDefinition

interface KommandoManager {
    val injector: Injector
    val formatters: List<MessageFormatter>
    val serializers: List<ArgumentResolver>
    val commands: List<CommandDefinition>
    val modules: List<Module>
}

