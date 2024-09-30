package utils

import java.awt.Color
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import javax.net.ssl.HttpsURLConnection

class DiscordWebhook(private val url: String) {
    var content: String? = null
    var username: String? = null
    var avatarUrl: String? = null
    var tts: Boolean = false
    val embeds: MutableList<EmbedObject> = mutableListOf()
    val attachments: MutableList<File> = mutableListOf()

    @Throws(IOException::class)
    fun execute() {
        if (content == null && embeds.isEmpty() && attachments.isEmpty()) {
            throw IllegalArgumentException("Set content or add at least one EmbedObject")
        }

        val json = JSONObject()

        json.put("content", content)
        json.put("username", username)
        json.put("avatar_url", avatarUrl)
        json.put("tts", tts)

        if (embeds.isNotEmpty()) {
            val embedObjects = mutableListOf<JSONObject>()

            for (embed in embeds) {
                val jsonEmbed = JSONObject()

                jsonEmbed.put("title", embed.title)
                jsonEmbed.put("description", embed.description)
                jsonEmbed.put("url", embed.url)
                embed.color?.let { color ->
                    val rgb = color.red shl 16 or (color.green shl 8) or color.blue
                    jsonEmbed.put("color", rgb)
                }

                embed.footer?.let { footer ->
                    val jsonFooter = JSONObject()
                    jsonFooter.put("text", footer.text)
                    jsonFooter.put("icon_url", footer.iconUrl)
                    jsonEmbed.put("footer", jsonFooter)
                }

                embed.image?.let { image ->
                    val jsonImage = JSONObject()
                    jsonImage.put("url", image.url)
                    jsonEmbed.put("image", jsonImage)
                }

                embed.thumbnail?.let { thumbnail ->
                    val jsonThumbnail = JSONObject()
                    jsonThumbnail.put("url", thumbnail.url)
                    jsonEmbed.put("thumbnail", jsonThumbnail)
                }

                embed.author?.let { author ->
                    val jsonAuthor = JSONObject()
                    jsonAuthor.put("name", author.name)
                    jsonAuthor.put("url", author.url)
                    jsonAuthor.put("icon_url", author.iconUrl)
                    jsonEmbed.put("author", jsonAuthor)
                }

                val jsonFields = mutableListOf<JSONObject>()
                embed.fields.forEach { field ->
                    val jsonField = JSONObject()
                    jsonField.put("name", field.name)
                    jsonField.put("value", field.value)
                    jsonField.put("inline", field.inline)
                    jsonFields.add(jsonField)
                }
                jsonEmbed.put("fields", jsonFields.toTypedArray())
                embedObjects.add(jsonEmbed)
            }

            json.put("embeds", embedObjects.toTypedArray())
        }

        val boundary = "----WebKitFormBoundary" + System.currentTimeMillis()
        val url = URI(this.url).toURL()
        val connection = url.openConnection() as HttpsURLConnection
        connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
        connection.addRequestProperty("User-Agent", "Kotlin-DiscordWebhook-BY-Lished")
        connection.doOutput = true
        connection.requestMethod = "POST"

        val stream: OutputStream = connection.outputStream

        stream.write("--$boundary\r\n".toByteArray())
        stream.write("Content-Disposition: form-data; name=\"payload_json\"\r\n\r\n".toByteArray())
        stream.write(json.toString().toByteArray())
        stream.write("\r\n".toByteArray())

        attachments.forEach {
            stream.write("--$boundary\r\n".toByteArray())
            stream.write("Content-Disposition: form-data; name=\"file\"; filename=\"${it.name}\"\r\n".toByteArray())
            stream.write("Content-Type: application/octet-stream\r\n\r\n".toByteArray())
            stream.write(it.readBytes())
            stream.write("\r\n".toByteArray())
        }
        stream.write("--$boundary--\r\n".toByteArray())

        stream.flush()
        stream.close()

        connection.inputStream.close()
        connection.disconnect()
    }

    class EmbedObject {
        var title: String? = null
        var description: String? = null
        var url: String? = null
        var color: Color? = null
        var footer: Footer? = null
        var thumbnail: Thumbnail? = null
        var image: Image? = null
        var author: Author? = null
        val fields: MutableList<Field> = mutableListOf()

        fun setTitle(title: String): EmbedObject {
            this.title = title
            return this
        }

        fun setDescription(description: String): EmbedObject {
            this.description = description
            return this
        }

        fun setUrl(url: String): EmbedObject {
            this.url = url
            return this
        }

        fun setColor(color: Color): EmbedObject {
            this.color = color
            return this
        }

        fun setFooter(text: String, icon: String): EmbedObject {
            this.footer = Footer(text, icon)
            return this
        }

        fun setThumbnail(url: String): EmbedObject {
            this.thumbnail = Thumbnail(url)
            return this
        }

        fun setImage(url: String): EmbedObject {
            this.image = Image(url)
            return this
        }

        fun setAuthor(name: String, url: String, icon: String): EmbedObject {
            this.author = Author(name, url, icon)
            return this
        }

        fun addField(name: String, value: String, inline: Boolean): EmbedObject {
            this.fields.add(Field(name, value, inline))
            return this
        }

        class Footer(val text: String, val iconUrl: String)
        class Thumbnail(val url: String)
        class Image(val url: String)
        class Author(val name: String, val url: String, val iconUrl: String)
        class Field(val name: String, val value: String, val inline: Boolean)
    }

    private class JSONObject {
        private val map: MutableMap<String, Any?> = HashMap()

        fun put(key: String?, value: Any?) {
            if (value != null) {
                map[key!!] = value
            }
        }

        override fun toString(): String {
            val builder = StringBuilder()
            val entrySet = map.entries
            builder.append("{")
            var i = 0
            for (entry in entrySet) {
                val value = entry.value
                builder.append(quote(entry.key)).append(":")
                when (value) {
                    is String -> builder.append(quote(value))
                    is Int -> builder.append(value)
                    is Boolean -> builder.append(value)
                    is JSONObject -> builder.append(value.toString())
                    is Array<*> -> {
                        builder.append("[")
                        val len = value.size
                        for (j in 0 until len) {
                            builder.append(value[j].toString()).append(if (j != len - 1) "," else "")
                        }
                        builder.append("]")
                    }
                }
                builder.append(if (++i == entrySet.size) "}" else ",")
            }
            return builder.toString()
        }

        private fun quote(string: String?): String {
            return "\"$string\""
        }
    }
}