package dev.helight.kommando

import com.google.inject.Module
import dev.helight.kommando.command.CommandInvoker
import dev.helight.kommando.resolvers.MentionResolver
import io.github.deck.core.DeckClient
import io.github.deck.core.event.message.MessageCreateEvent
import io.github.deck.core.util.on

class GuildedCommandManager(val client: DeckClient): KommandoManagerBase() {


    init {
        val selfRef = this
        modules.add(Module { binder ->
            binder.bind(GuildedCommandManager::class.java).toProvider { selfRef }
            binder.bind(DeckClient::class.java).toProvider { client }
        })
        serializers.add(MentionResolver())

        client.on<MessageCreateEvent> {
            handle(this)
        }
    }

    suspend fun handle(event: MessageCreateEvent) {
        if (event.message.content.startsWith("!")) {
            val command = event.message.content.removePrefix("!")
            val scope = GuildedServerChatScope(event)
            val context = GuildedMessageContext(event.message, command)
            val definition = findCommand(context, scope)
            if (definition == null) {
                context.reply("Sorry I don't understand (Unknown Command)")
                return
            }
            val result = CommandInvoker.invoke(this, definition, context, scope)
            if (result.state != CommandResultState.NoContent) {
                context.reply(result.content)
            }
        }
    }

}