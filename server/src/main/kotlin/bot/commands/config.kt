package bot.commands

import bot.StatusChannel
import bot.StatusChannel.initialize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.jakejmattson.discordkt.commands.commands

fun config() = commands("Config") {
    slash("config", "Configure the channel for the bot to post in.") {
        execute {
            respond("Config successfully set to this channel.")
            StatusChannel.channel = channel
            CoroutineScope(Dispatchers.IO).launch {
                initialize()
            }
        }
    }
}