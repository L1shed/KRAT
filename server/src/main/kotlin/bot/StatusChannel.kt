package bot

import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

object StatusChannel {
    lateinit var channel: MessageChannel
    lateinit var message: Message
    val status = mutableMapOf<String, Boolean>()

    suspend fun initialize() {
        channel.messages.toList().forEach { if (it.author?.isBot == true) it.delete() }

        message = channel.createEmbed {
            title = "Computers Status"
            description = if (status.entries.isEmpty()) "No clients connected" else status.entries.joinToString("\n") { (client, online) ->
                (if (online) Emojis.greenCircle else Emojis.redCircle).toString() + " : **${client}**"
            }
        }
        status["john"] = true
        send()
    }

    suspend fun send() {
        if (!::message.isInitialized) return

        message.edit {
            embed {
                title = "Computers Status"
                description = if (status.entries.isEmpty()) "No clients connected" else status.entries.joinToString("\n") { (client, online) ->
                    (if (online) Emojis.greenCircle else Emojis.redCircle).toString() + " : **${client}**"
                }
            }
        }

    }

    fun online(user: String) {
        status[user] = true
        CoroutineScope(Dispatchers.IO).launch {
            send()
        }
    }

    fun offline(user: String) {
        status[user] = false
        CoroutineScope(Dispatchers.IO).launch {
            send()
        }
    }
}