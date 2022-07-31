package dev.helight.kommando

interface KommandoPermissionNode {

    val identifier: String
    suspend fun hasPermissions(permission: String): Boolean

}