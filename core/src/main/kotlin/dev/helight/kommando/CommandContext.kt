package dev.helight.kommando

import com.google.inject.Module

interface CommandContext {

    val fragments: List<String>
    var args: CommandArgs

    fun getPermissionNode(): KommandoPermissionNode? = null
    fun getModules(): Collection<Module>

    fun reply(message: String)

}

