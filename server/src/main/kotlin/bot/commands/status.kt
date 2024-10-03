package bot.commands

import dev.kord.x.emoji.Emojis
import me.jakejmattson.discordkt.arguments.ChoiceArg
import me.jakejmattson.discordkt.commands.commands

fun status() = commands("Status") {
    val clientsStatus = mapOf<String, Boolean>( //dummy data
        "client1" to true,
        "client2" to false,
        "John" to false,
        "drsna" to true
    )

    slash("computers", "Get the status of the computers.") {
        execute {
            respondPublic(
                "# Computers\n" + clientsStatus.entries.joinToString("\n") { (client, online) ->
                    (if (online) Emojis.greenCircle else Emojis.redCircle).toString() + " : **$client**"
                }
            )
        }
    }

    slash("lookup", "Get the information about a client.") {
        execute(ChoiceArg("client", "The client to lookup.", *clientsStatus.keys.toTypedArray())) {
            val choice = args.first
            respond(
                if (clientsStatus[choice] == true) Emojis.greenCircle else Emojis.redCircle
            )
        }
    }
}