package dev.helight.kommando.annotations

import kotlin.reflect.KClass

/**
 * Defines a scope guard for a command by checking if the
 * command's scope is assignable from [scopeClass].
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scope(val scopeClass: KClass<*>)