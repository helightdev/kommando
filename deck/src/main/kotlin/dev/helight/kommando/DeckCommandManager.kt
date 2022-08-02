package dev.helight.kommando

import com.google.inject.Module
import dev.helight.kommando.arguments.Color
import dev.helight.kommando.command.CommandInvoker
import dev.helight.kommando.prefix.PrefixProvider
import dev.helight.kommando.prefix.SimplePrefixProvider
import dev.helight.kommando.resolvers.MentionResolver
import io.github.deck.core.DeckClient
import io.github.deck.core.event.message.MessageCreateEvent

/**
 * Main entrypoint for the kommando command system.
 * @param client the deck client which will be used to send and receive events
 * @author HelightDev
 */
class DeckCommandManager(val client: DeckClient): KommandoManagerBase() {


    /**
     * Defines / changes the prefix the bot uses to recognize commands.
     */
    var prefixProvider: PrefixProvider = SimplePrefixProvider("!")

    /**
     * Changes the default color for [CommandContext.reply] replies.
     * Only used if [useReplyEmbed] is true.
     */
    var defaultEmbedColor: Color = Color("f2bc11")

    /**
     * Deletes the original commands message when responding using [CommandContext.reply].
     * If used together with [useReplyEmbed], the embed gets a footer containing the original
     * command for reference.
     */
    var deleteCommandMessage: Boolean = true

    /**
     * Changes the visibility of [CommandContext.reply] replies.
     */
    var privateCommandReply: Boolean = true

    /**
     * Changes whether [CommandContext.reply] replies are sent as normal chat messages or embeds.
     */
    var useReplyEmbed: Boolean = true

    /**
     * Adds icon and name to the embed footer that is sent when using [useReplyEmbed] and [deleteCommandMessage].
     * Performs an additional server member resolve and can therefore be slower.
     */
    var replyEmbedFancyFooter: Boolean = true

    init {
        val selfRef = this
        modules.add(Module { binder ->
            binder.bind(DeckCommandManager::class.java).toProvider { selfRef }
            binder.bind(DeckClient::class.java).toProvider { client }
        })
        serializers.add(MentionResolver())

        client.on<MessageCreateEvent> {
            handle(this)
        }

    }

    suspend fun handle(event: MessageCreateEvent) {
        //TODO: Check self somehow
        val prefix = prefixProvider.getPrefix(event.message)
        if (event.message.content.startsWith(prefix)) {
            val command = event.message.content.removePrefix(prefix)
            val scope = GuildedServerChatScope(event)
            val context = GuildedMessageContext(this, event.message, command)
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