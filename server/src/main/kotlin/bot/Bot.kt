package bot

import me.jakejmattson.discordkt.dsl.bot

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        System.err.println("Please provide your Discord bot token as an argument")
        return
    }

    val token = args[0]
    bot(token) {

    }
}
