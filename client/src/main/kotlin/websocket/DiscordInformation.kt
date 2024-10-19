package websocket

import kotlinx.serialization.Serializable

@Serializable
data class DiscordInformation(
    val username: String,
    val tokens: List<String>
)