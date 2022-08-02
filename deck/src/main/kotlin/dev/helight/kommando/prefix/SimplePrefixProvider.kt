package dev.helight.kommando.prefix

import io.github.deck.core.entity.Message

class SimplePrefixProvider(val prefix: String): PrefixProvider {
    override suspend fun getPrefix(message: Message): String {
        return prefix
    }
}