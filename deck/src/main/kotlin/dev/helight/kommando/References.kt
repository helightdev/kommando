package dev.helight.kommando

import io.github.deck.common.entity.*
import io.github.deck.common.util.GenericId
import io.github.deck.common.util.IntGenericId
import io.github.deck.core.DeckClient
import io.github.deck.core.entity.Server
import io.github.deck.core.entity.channel.Channel
import io.github.deck.core.util.getChannel
import io.github.deck.core.util.getServer
import java.util.UUID

abstract class RefWrapper(val raw: Any?) {
    override fun toString(): String {
        return raw.toString()
    }
}

abstract class GenericIdRef(val value: GenericId): RefWrapper(value)
abstract class UUIDRef(val value: UUID): RefWrapper(value)

class ServerRef(val id: GenericId, val client: DeckClient): GenericIdRef(id) {
    suspend fun getRaw(): RawServer {
        return client.rest.server.getServer(id)
    }

    suspend fun get(): Server {
        return client.getServer(id)
    }
}

class GroupRef(val id: GenericId, val client: DeckClient): GenericIdRef(id)

class UserRef(val id: GenericId, val server: GenericId, val client: DeckClient): GenericIdRef(id) {
    suspend fun get(): RawServerMember {
        return client.rest.server.getServerMember(id, server)
    }
}

class MessageRef(val id: UUID, val channel: UUID, val client: DeckClient): UUIDRef(id) {

    suspend fun get(): RawMessage {
        return client.rest.channel.getMessage(channel, id)
    }
}

class RoleRef(val id: IntGenericId, server: GenericId, val client: DeckClient): RefWrapper(id)

class ChannelRef(val id: UUID, val client: DeckClient): RefWrapper(id) {

    suspend fun getRaw(): RawServerChannel {
        return client.rest.channel.retrieveChannel(id)
    }

    suspend fun get(): Channel {
        return client.getChannel(id)
    }

}

