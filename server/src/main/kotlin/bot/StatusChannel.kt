package bot

import dev.kord.common.entity.DiscordGuild
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.Channel
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.embed
import dev.kord.x.emoji.Emojis
import io.ktor.util.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import sun.rmi.runtime.Log
import kotlin.collections.component1
import kotlin.collections.component2

object StatusChannel {
    val status = mutableMapOf<String, Boolean>()

    private lateinit var message: Message

    suspend fun initialize(channel: TextChannel) {
        channel.messages.toList().forEach { if (it.author?.isBot == true) it.delete() }

        message = channel.createEmbed {
            title = "Computers Status"
            description = if (status.entries.isEmpty()) "No clients connected" else status.entries.joinToString("\n") { (client, online) ->
                (if (online) Emojis.greenCircle else Emojis.redCircle).toString() + " : **${client}**"
            }
        }
    }

    suspend fun update(user: String) {
        status[user] = !status.getOrDefault(user, false)
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
}