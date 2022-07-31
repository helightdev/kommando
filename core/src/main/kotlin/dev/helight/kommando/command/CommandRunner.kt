package dev.helight.kommando.command

class CommandRunner<T>(val block: suspend CommandRunContext.() -> T) {
    suspend fun run(context: CommandRunContext): T {
        return block(context)
    }
}