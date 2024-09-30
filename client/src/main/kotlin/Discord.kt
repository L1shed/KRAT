import Main.user
import utils.DiscordWebhook

object Discord {
    fun sendWebhook(): DiscordWebhook {
        return DiscordWebhook("https://discord.com/api/webhooks/1289937157145886800/dz8f1OAp8FczOWgGUe78fJSxi4Ogv3XySkoemeiaWazv680BUYWDDng83Ob7N_yGigJJ").apply {
            username = user
        }
    }
}