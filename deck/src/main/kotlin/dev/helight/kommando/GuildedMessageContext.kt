package dev.helight.kommando

import com.google.inject.Module
import dev.helight.kommando.modules.MessageModule
import dev.helight.kommando.modules.ServerModule
import io.github.deck.common.entity.RawEmbed
import io.github.deck.common.entity.RawMessage
import io.github.deck.common.util.OptionalProperty
import io.github.deck.common.util.asNullable
import io.github.deck.common.util.isPresent
import io.github.deck.core.DeckClient
import io.github.deck.core.entity.Message
import io.github.deck.core.util.sendReply
import io.github.deck.rest.builder.SendMessageRequestBuilder
import io.github.deck.rest.request.SendMessageRequest
import io.github.deck.rest.request.SendMessageResponse
import io.github.deck.rest.util.sendRequest
import io.ktor.http.*
import java.util.*

class GuildedMessageContext(val manager: DeckCommandManager, val delegate: Message, val content: String): CommandContext {

    override val fragments = content.split(" ")
    override var args: CommandArgs = CommandArgs(fragments)

    override fun getModules(): Collection<Module> {
        return listOf(ServerModule(delegate.client, delegate.serverId!!), MessageModule(delegate))
    }

    override suspend fun reply(message: String) {
        if (manager.useReplyEmbed) {
            if (manager.replyEmbedFancyFooter) {
                val member = try {
                    delegate.client.rest.server.getServerMember(delegate.serverId!!, delegate.authorId)
                } catch (e: Exception) {
                    println("Get Server Member Error, fuck: ${delegate.authorId}@${delegate.serverId!!}")
                    e.printStackTrace()
                    error("")
                }
                val user = member.user
                val avatar = user.avatar.asNullable()
                val name = member.nickname.asNullable() ?: user.name

                delegate.client.sendFixedMessage(delegate.channelId) {
                    replyTo(delegate.id)
                    if (manager.privateCommandReply) isPrivate = true
                    embed {
                        description = message
                        color = manager.defaultEmbedColor.decimal
                        if (manager.deleteCommandMessage) footer {
                            iconUrl = avatar
                            text = "$name: ${delegate.content}"
                        }
                    }
                }
            } else {
                delegate.client.sendFixedMessage(delegate.channelId) {
                    replyTo(delegate.id)
                    if (manager.privateCommandReply) isPrivate = true
                    embed {
                        description = message
                        color = manager.defaultEmbedColor.decimal
                        if (manager.deleteCommandMessage) footer {
                            text = "Command: ${delegate.content}"
                        }
                    }
                }
            }
        } else {
            delegate.client.sendFixedMessage(delegate.channelId) {
                replyTo(delegate.id)
                if (manager.privateCommandReply) isPrivate = true
                content = message
            }
        }
        if (manager.deleteCommandMessage) delegate.delete()
    }
}

//TODO: Replace after api is more stable
suspend fun DeckClient.sendFixedMessage(
    channelId: UUID,
    builder: SendMessageRequestBuilder.() -> Unit
): RawMessage = rest.channel.sendMessage(channelId, builder)