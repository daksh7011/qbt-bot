package qbtbot.util

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.env
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters
import kotlin.math.round

val testGuild: Snowflake get() = Snowflake(env(Environment.TEST_GUILD_ID).toLong())

fun Message.isBot(): Boolean = author?.isBot != false
fun Message.isNotBot(): Boolean = isBot().not()
fun Snowflake.isOwner(): Boolean = toString() == env(Environment.OWNER_ID)

suspend fun Message.getEmbedFooter(): EmbedBuilder.Footer = this.kord.prepareEmbedFooter()

suspend fun Kord.prepareEmbedFooter(): EmbedBuilder.Footer = EmbedBuilder.Footer().apply {
    text = "Powered by ${getUser(selfId)?.username}"
    icon = getUser(selfId)?.avatar?.cdnUrl?.toUrl()
}

fun String?.bold(): String = "**$this**"

fun String?.italic(): String = "*$this*"

suspend fun ExtensibleBot.loginToBackend() {
    val response = requestQbitTorrentForLogin()
    logger.info { "qBitTorrent login -> status: ${response.status}" }
}

suspend fun requestQbitTorrentForLogin(): HttpResponse =
    // try to log in to qBitTorrent and save cookie
    httpClient.submitForm(
        url = "$BASE_URL/auth/login",
        formParameters = parameters {
            append("username", env(Environment.USERNAME))
            append("password", env(Environment.PASSWORD))
        }
    )

@Suppress("MagicNumber")
fun Double.round(decimals: Int = 2): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}
