package dev.helight.kommando.arguments

data class ArgumentContext(
    val serializer: ArgumentResolver,
    val type: Class<*>,
    val modifications: List<Any>,
    val name: String
) {

    inline fun <reified T> has(): Boolean {
        return modifications.any { it is T }
    }

}