package dev.helight.kommando.prefix

import io.github.deck.core.entity.Message

interface PrefixProvider {

    suspend fun getPrefix(message: Message): String

}

