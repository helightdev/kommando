package dev.helight.kommando

import dev.helight.kommando.command.CommandRunContext
import io.github.deck.common.util.GenericId
import io.github.deck.core.DeckClient
import io.github.deck.core.entity.Message

val CommandRunContext.client: DeckClient
    get() = get()

val CommandRunContext.message: Message
    get() = get()

val CommandRunContext.channel: ChannelRef
    get() = get()

val CommandRunContext.server: ServerRef?
    get() = tryGet()

val CommandRunContext.user: UserRef?
    get() = tryGet()

val CommandRunContext.selfId: GenericId
    get() = client.selfId