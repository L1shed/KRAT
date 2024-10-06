package websocket

import io.ktor.websocket.DefaultWebSocketSession
import java.util.concurrent.atomic.AtomicInteger

data class Connection(
    val session: DefaultWebSocketSession,
    val id: Int = lastId.getAndIncrement(),
    val name: String = "user${id}"
) {
    companion object {
        var lastId = AtomicInteger(0)
    }
}