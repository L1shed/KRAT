package websocket

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val timestamp: Long,
)
