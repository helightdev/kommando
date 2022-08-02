package dev.helight.kommando.command

import com.google.inject.Injector
import dev.helight.kommando.*
import dev.helight.kommando.annotations.Syntax
import dev.helight.kommando.arguments.ArgumentContext
import kotlinx.coroutines.*
import java.lang.reflect.Method
import kotlin.coroutines.Continuation
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.*

class ReflectiveCommand(private val instance: Any? = null, private val method: Method, private val manager: KommandoManager) :
    KommandoBaseCommand(resolveKommandoPath(method), resolveKommandoScope(method)) {

    override val argumentContexts: List<ArgumentContext>
    override val syntaxDescription: String

    init {
        method.trySetAccessible()
        val serializers = manager.serializers.sortedByDescending { it.priority }
        argumentContexts = method.parameters.map { param ->
            val serializer = serializers.firstOrNull() { it.checkParameter(param) } ?: error("No parameter serializer found for $param")
            ArgumentContext(
                serializer, param.type, param.annotations.mapNotNull { annotation ->
                    serializer.annotationMappers().firstOrNull { mapper -> mapper.accepts(annotation) }
                        ?.resolve(annotation)
                }, param.name
            )
        }
        val syntaxMessage = method.getAnnotation(Syntax::class.java)?.text ?: argumentContexts
            .filterNot { it.serializer.isContextOnly }
            .joinToString(" ") { "<${it.name}>" }
            .let { "${path.joinToString(" ")} $it" }
        syntaxDescription = "Can't process user input. Command Syntax: $syntaxMessage"
    }

    override suspend fun call(arguments: List<Any?>, injector: Injector): Any? = coroutineScope {
        val kFun = method.kotlinFunction
        val rawResult = if (kFun != null) {
            if (kFun.isSuspend) {
                val args: Array<Any?>
                if (Continuation::class.java.isAssignableFrom(argumentContexts.last().type)) {
                    args = arrayOf(instance, *(arguments.take(arguments.size - 1)).toTypedArray())
                } else {
                    args = arrayOf(instance, *arguments.toTypedArray())
                }
                kFun.callSuspend(*args)
            } else {
                kFun.call(instance, *arguments.toTypedArray())
            }
        } else {
            method.invoke(instance, *arguments.toTypedArray())
        }
        reduceResult(rawResult, injector)
    }

    private suspend fun reduceResult(value: Any?, injector: Injector): Any? = when(value) {
        Unit -> null
        is CommandRunner<*> -> reduceResult(value.run(CommandRunContext(injector)), injector)
        else -> value
    }
}

