package websocket

import kotlinx.serialization.Serializable

@Serializable
data class Instruction(
    val command: String,
    val arguments: List<String>
)