package dev.helight.kommando

import com.google.inject.*

interface CommandScope {

    val name: String

    fun getModules(): Collection<Module>

}