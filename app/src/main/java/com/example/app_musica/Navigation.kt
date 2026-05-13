package com.example.app_musica

import kotlinx.serialization.Serializable

@Serializable
object UniqueHomeRoute

@Serializable
data class UniqueDetailRoute(val albumId: String)
