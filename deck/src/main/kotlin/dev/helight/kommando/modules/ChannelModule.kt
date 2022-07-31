package dev.helight.kommando.modules

import com.google.inject.Binder
import com.google.inject.Module
import dev.helight.kommando.ChannelRef
import io.github.deck.core.event.message.MessageCreateEvent

class ChannelModule(val event: MessageCreateEvent): Module {
    override fun configure(binder: Binder) {
        binder.bind(ChannelRef::class.java).toProvider { ChannelRef(event.channelId, event.client) }
    }
}