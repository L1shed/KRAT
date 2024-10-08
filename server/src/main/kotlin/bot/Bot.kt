package bot

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jakejmattson.discordkt.dsl.bot
import websocket.Application.module

fun main() {
    CoroutineScope(Dispatchers.IO).launch {
        bot(System.getenv("DISCORD_BOT_TOKEN")) {}
    }

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}
