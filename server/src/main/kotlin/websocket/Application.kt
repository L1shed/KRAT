package websocket

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(WebSockets) {
            pingPeriod = 15.seconds
            timeout = 15.seconds
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }

        routing {
            // TODO: private channels
            webSocket("/pool") {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    when (frame.readText()) {
                        "ping" -> send(Frame.Text("pong"))
                        "close" -> close(CloseReason(CloseReason.Codes.NORMAL, "Client said CLOSE"))
                        else -> send(Frame.Text("Unknown command ${frame.readText()}"))
                    }
                }
            }
        }

    }.start(wait = true)
}