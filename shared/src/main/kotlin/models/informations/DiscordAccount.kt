package models.informations

import kotlinx.serialization.Serializable

@Serializable
data class DiscordAccount (
    val username: String,
    val tokens: List<String>
)