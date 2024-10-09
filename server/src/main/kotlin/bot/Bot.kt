package bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.TextChannel
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import me.jakejmattson.discordkt.dsl.bot
import websocket.Application.module
import kotlin.system.exitProcess

fun main() {
    CoroutineScope(Dispatchers.IO).launch {
        bot(System.getenv("DISCORD_BOT_TOKEN")) {
            onStart {
                if (kord.guilds.count() > 1) {
                    System.err.println("Bot is in more than 1 server. Shutting it down to protect from leaks")
                    exitProcess(1)
                }
                StatusChannel.initialize(
                    channel =  kord.getChannel(Snowflake(1292106820051013682)) as TextChannel // TODO: Make channel configurable
                )
            }
        }
    }

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}
