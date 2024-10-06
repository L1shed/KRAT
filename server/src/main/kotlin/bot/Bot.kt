package bot

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jakejmattson.discordkt.dsl.bot
import websocket.module

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        System.err.println("Please provide your Discord bot token as an argument")
        return
    }

    CoroutineScope(Dispatchers.IO).launch {
        bot(args.first()) {}
    }

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)

}
