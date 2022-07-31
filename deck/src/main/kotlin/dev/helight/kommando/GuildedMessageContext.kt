package dev.helight.kommando

import com.google.inject.Module
import dev.helight.kommando.modules.MessageModule
import dev.helight.kommando.modules.ServerModule
import io.github.deck.core.entity.Message
import io.github.deck.core.util.sendReply
import kotlinx.coroutines.runBlocking

class GuildedMessageContext(val delegate: Message, val content: String): CommandContext {

    override val fragments = content.split(" ")
    override var args: CommandArgs = CommandArgs(fragments)

    override fun getModules(): Collection<Module> {
        return listOf(ServerModule(delegate.client, delegate.serverId!!), MessageModule(delegate))
    }

    override fun reply(message: String) {
        runBlocking {
            delegate.sendReply(message)
        }
    }
}