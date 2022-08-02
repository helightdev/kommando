package dev.helight.kommando.annotations

/**
 * Manually defines the syntax message body
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Syntax(val text: String)

