package dev.helight.kommando.resolvers

import com.google.inject.Injector
import dev.helight.kommando.*
import dev.helight.kommando.annotations.MentionMapper
import dev.helight.kommando.annotations.Mention
import dev.helight.kommando.arguments.ArgumentResolverBase
import dev.helight.kommando.arguments.ArgumentContext
import dev.helight.kommando.command.CommandDefinition
import io.github.deck.core.DeckClient
import io.github.deck.core.entity.Mentions
import java.util.*

class MentionResolver: ArgumentResolverBase(100, true) {

    init {
        associatedAnnotations.add(Mention::class.java)
        annotationMappers.add(MentionMapper())
    }

    override fun deserialize(
        definition: CommandDefinition,
        context: CommandContext,
        scope: CommandScope,
        injector: Injector,
        argumentContext: ArgumentContext,
        args: Stack<String>,
    ): Any? {
        val mentions = injector.tryGetInstance(Mentions::class.java) ?: return null
        val type = argumentContext.type
        if (UserRef::class.java.isAssignableFrom(type)) {
            val serverScope = scope as ServerScope? ?: return null
            val client = injector.getInstance(DeckClient::class.java)
            val user = mentions.users.firstOrNull() ?: return null
            return UserRef(user, serverScope.serverID, client)
        } else if (ChannelRef::class.java.isAssignableFrom(type)) {
            val client = injector.getInstance(DeckClient::class.java)
            val channel = mentions.channels.firstOrNull() ?: return null
            return ChannelRef(channel, client)
        } else if (RoleRef::class.java.isAssignableFrom(type)) {
            val serverScope = scope as ServerScope? ?: return null
            val client = injector.getInstance(DeckClient::class.java)
            val role = mentions.roles.firstOrNull() ?: return null
            return RoleRef(role, serverScope.serverID, client)
        }
        return null
    }

}