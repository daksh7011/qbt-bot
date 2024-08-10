package qbtbot.util

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.EmbedBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

val testGuild: Snowflake get() = Snowflake(env(Environment.TEST_GUILD_ID).toLong())

fun Message.isBot(): Boolean = author?.isBot != false
fun Message.isNotBot(): Boolean = isBot().not()
fun Snowflake.isOwner(): Boolean = toString() == env(Environment.OWNER_ID)

suspend fun Message.getEmbedFooter(): EmbedBuilder.Footer = this.kord.prepareEmbed()

suspend fun Kord.prepareEmbed(): EmbedBuilder.Footer = EmbedBuilder.Footer().apply {
    text = "Powered by ${getUser(selfId)?.username}"
    icon = getUser(selfId)?.avatar?.cdnUrl?.toUrl()
}

fun String?.bold(): String = "**$this**"

fun String?.italic(): String = "*$this*"

suspend fun ExtensibleBot.loginToBackend() {
    // try to log in to qBitTorrent
    val response = httpClient.submitForm(
        url = "$BASE_URL/auth/login",
        formParameters = parameters {
            append("username", env(Environment.USERNAME))
            append("password", env(Environment.PASSWORD))
        }
    )
    logger.info { "qBitTorrent login -> status: ${response.status}" }
}
