package dev.helight.kommando

import com.google.inject.Guice
import com.google.inject.Module
import dev.helight.kommando.arguments.resolvers.*
import dev.helight.kommando.command.CommandDefinition
import dev.helight.kommando.command.ReflectiveCommand
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.full.companionObjectInstance

abstract class KommandoManagerBase: KommandoManager {

    override val injector = Guice.createInjector(Module {
        it.bind(KommandoManager::class.java).toProvider { this } // Default bind the manager interface to current instance
    })
    override val serializers = mutableListOf(
        InjectionResolver(),
        StringResolver(),
        IntResolver(),
        FloatResolver(),
        LongResolver(),
        DoubleResolver(),
        EnumResolver(),
        BooleanResolver(),
        ColorResolver()
    )
    override val commands = mutableListOf<CommandDefinition>()
    override val modules = mutableListOf<Module>()
    override val formatters = mutableListOf<MessageFormatter>()

    fun registerMethod(instance: Any?, method: Method) {
        commands.add(ReflectiveCommand(instance, method, this))
    }

    fun registerClasses(clazz: Class<*>) {
        traverseClassesDownwards(clazz) {
            val kClass = it.kotlin
            var instance: Any? = null

            if (kClass.isCompanion) {
                instance = kClass.companionObjectInstance
            } else {
                instance = it.declaredFields
                    .firstOrNull { field -> field.name.equals("instance", ignoreCase = true)}
                    ?.get(null)
            }
            it.declaredMethods.filterNot { method -> method.modifiers and Modifier.PUBLIC == 0 }.forEach { method ->
                method.trySetAccessible()
                registerMethod(instance, method)
            }
        }
    }

}