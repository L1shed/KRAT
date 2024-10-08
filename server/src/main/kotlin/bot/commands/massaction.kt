package bot.commands

import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.commands.commands
import websocket.Application

fun massaction() = commands("Mass Action") {
    slash("request", "Every computers ping the given ip address/domain") {
        execute(AnyArg("address", "The address to ping")) {
            val address = args.first

            // TODO: Serialized Frame handling
            //  - proper instruction validation
            //  - typed s-object requests
            Application.sendAll("ping:$address")

        }
    }

    /*slash("run") {

    }*/

}