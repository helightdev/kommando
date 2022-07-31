package dev.helight.kommando.modules

import com.google.inject.Binder
import com.google.inject.Module
import dev.helight.kommando.ServerRef
import io.github.deck.common.util.GenericId
import io.github.deck.core.DeckClient

class ServerModule(val client: DeckClient, val id: GenericId): Module {
    override fun configure(binder: Binder) {
        binder.bind(ServerRef::class.java).toProvider { ServerRef(id, client) }
    }
}