package dev.helight.kommando.prefix

import dev.helight.kommando.ServerRef
import io.github.deck.core.entity.Message

abstract class ServerAwarePrefixProvider: PrefixProvider {

    override suspend fun getPrefix(message: Message): String {
        return getPrefix(ServerRef(message.serverId!!, message.client))
    }

    abstract suspend fun getPrefix(serverRef: ServerRef): String
}