package dev.helight.kommando.modules

import com.google.inject.Binder
import com.google.inject.Module
import dev.helight.kommando.MessageRef
import dev.helight.kommando.UserRef
import io.github.deck.core.entity.Mentions
import io.github.deck.core.entity.Message

class MessageModule(val message: Message): Module {
    override fun configure(binder: Binder) {
        if (message.mentions != null) binder.bind(Mentions::class.java).toProvider { message.mentions }
        binder.bind(UserRef::class.java).toProvider { UserRef(message.authorId, message.serverId!!, message.client) }
        binder.bind(MessageRef::class.java).toProvider { MessageRef(message.id, message.channelId, message.client) }
        binder.bind(Message::class.java).toProvider { message }
    }
}