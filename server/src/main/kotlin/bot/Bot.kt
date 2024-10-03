package bot

import io.github.cdimascio.dotenv.dotenv
import me.jakejmattson.discordkt.dsl.bot

fun main() {
    val env = dotenv()

    bot(env["BOT_TOKEN"]) {

    }
}
