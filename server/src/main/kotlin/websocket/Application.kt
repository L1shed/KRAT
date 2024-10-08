package websocket

import bot.StatusChannel
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText

object Application {
    private val connections = mutableSetOf<Connection>()

    fun Application.module() {
        install(WebSockets)

        routing {
            webSocket("/pool") {
                val thisConnection = Connection(this)
                connections += thisConnection
                StatusChannel.online(thisConnection.name)
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    when (frame.readText()) {
                        "ping" -> send(Frame.Text("pong"))
                        "close" -> close(CloseReason(CloseReason.Codes.NORMAL, "Client said CLOSE"))
                        else -> send(Frame.Text("Unknown command ${frame.readText()}"))
                    }
                }
                StatusChannel.offline(thisConnection.name)
                connections -= thisConnection
            }
        }
    }

    suspend fun sendAll(text: String) {
        connections.forEach {
            it.session.send(Frame.Text(text = text))
        }
    }
}