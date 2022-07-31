package dev.helight.kommando

import com.google.common.collect.Table
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.inject.Injector
import dev.helight.kommando.annotations.Command
import dev.helight.kommando.annotations.Scope
import dev.helight.kommando.command.CommandRunContext
import dev.helight.kommando.command.CommandRunner
import java.lang.reflect.Method

@DslMarker
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class KommandoDsl

@KommandoDsl
fun <T> command(block: suspend CommandRunContext.() -> T) = CommandRunner(block)

fun <T> Injector.tryGetInstance(clazz: Class<T>): T? {
    return try {
        getInstance(clazz)
    } catch (missing: Exception) { null }
}

fun Injector.tryGetInstanceUnsafe(clazz: Class<*>): Any? {
    return try {
        getInstance(clazz)
    } catch (missing: Exception) { null }
}

fun resolveKommandoPath(method: Method): List<String> = traverseMethodClassesUpwards<Command?>(method) {
    it.getAnnotation(Command::class.java)
}.asReversed().toMutableList().apply {
    add(method.getAnnotation(Command::class.java))
}.filterNotNull().map { it.name }.flatMap { it.split(" ") }

fun resolveKommandoScope(method: Method): Class<*> {
    val data = mutableListOf<Class<*>?>()
    data.add(method.getAnnotation(Scope::class.java)?.scopeClass?.java)
    data.addAll(traverseMethodClassesUpwards<Class<*>?>(method) {
        it.getAnnotation(Scope::class.java)?.scopeClass?.java
    })
    return data.filterNotNull().firstOrNull() ?: Any::class.java
}

fun <T> traverseMethodClassesUpwards(method: Method, block: (Class<*>) -> T): List<T> {
    val data = mutableListOf<T>()
    var clazz = method.declaringClass
    data.add(block(clazz))
    while (true) {
        val declared = clazz.declaringClass ?: break
        clazz = declared
        data.add(block(clazz))
    }
    return data
}

fun <T> traverseClassesDownwards(clazz: Class<*>, block: (Class<*>) -> T): List<T> {
    val data = mutableListOf<T>()
    data.add(block(clazz))
    clazz.declaredClasses.forEach {
        data.addAll(traverseClassesDownwards(it, block))
    }
    return data
}

fun <A,B,C> Table<A, B, C>.serialize(): String {
    return Gson().toJson(rowMap())
}

fun <A,B,C> Table<A, B, C>.deserialize(string: String) {
    val token = object: TypeToken<Map<A, Map<B, C>>>(){}.type
    val rows = Gson().fromJson<Map<A, Map<B, C>>>(string, token)
    clear()
    rows.forEach { (a, m) ->
        m.forEach { (b, v) ->
            if (v != null) {
                put(a, b, v)
            }
        }
    }
}