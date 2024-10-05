package websocket

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.runBlocking
import java.util.Scanner
import kotlin.time.Duration.Companion.seconds

fun main() {
    val client = HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 20.seconds
        }
    }
    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/echo") {
            while(true) {
                val othersMessage = incoming.receive() as? Frame.Text
                println(othersMessage?.readText())
                val myMessage = Scanner(System.`in`).next()
                if(myMessage != null) {
                    send(Frame.Text(myMessage))
                }
            }
        }
    }
    client.close()
}