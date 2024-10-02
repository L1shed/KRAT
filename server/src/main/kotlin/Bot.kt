import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import io.github.cdimascio.dotenv.dotenv

suspend fun main() {
    val env = dotenv()

    val kord = Kord(env["BOT_TOKEN"])

    val clientsStatus = mapOf<String, Boolean>( //dummy data
        "client1" to true,
        "client2" to false,
        "John" to false,
        "drsna" to true
    )

    val computers = kord.createGuildChatInputCommand(
        guildId = Snowflake(env["GUILD_ID"]),
        name = "computers",
        description = "get the status of the victims' computers"
    )

    kord.createGuildChatInputCommand(
        guildId = Snowflake(env["GUILD_ID"]),
        name = "users",
        description = "geot the status of the victims' computers"
    )

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        interaction.respondPublic {
            content = ""
            println(interaction.command)
            println(computers)
            clientsStatus.forEach { name, online ->
                content+="${if (online) ":green_circle:" else ":red_circle:"} **$name**\n"
            }
        }
    }

    kord.login()
}
