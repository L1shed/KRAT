package bot.commands

import me.jakejmattson.discordkt.arguments.AnyArg
import me.jakejmattson.discordkt.commands.commands

fun massaction() = commands("Mass Action") {
    slash("request", "Every computers make a request to the given ip address/domain") {
        execute(AnyArg("address", "The address to ping")) {
            val address = args.first

            // TODO: Send instructions to all computers via websocket
        }
    }

    slash("run") {

    }

}