package com.maxrave.simpmusic.ui.navigation.destination.home

import kotlinx.serialization.Serializable

@Serializable
data class AIRadioDestination(
    val videoId: String? = null,
    val playlistId: String? = null,
)
