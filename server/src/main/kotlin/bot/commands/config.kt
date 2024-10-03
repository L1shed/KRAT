package bot.commands

import me.jakejmattson.discordkt.arguments.ChannelArg
import me.jakejmattson.discordkt.commands.commands

fun config() = commands("Config") {
    slash("config", "Configure the channel for the bot to post in.") {
        execute(ChannelArg) {
            val channel = args.first
            respond("Config successfully set to $channel")
        }
    }
}