package dev.helight.kommando

import com.google.inject.Module
import dev.helight.kommando.modules.ChannelModule
import io.github.deck.common.entity.RawServerChannel
import io.github.deck.common.util.GenericId
import io.github.deck.core.event.message.MessageCreateEvent
import java.util.UUID

interface ServerScope {
    val serverID: GenericId
}

class GuildedServerChatScope(val event: MessageCreateEvent): CommandScope, ServerScope {

    override val name: String
        get() = "ServerChat-${event.channelId}"

    override val serverID: GenericId
        get() = event.serverId!!

    override fun getModules(): Collection<Module> {
        return listOf(ChannelModule(event))
    }
}