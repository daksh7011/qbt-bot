package qbtbot.commands

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.chatCommand
import dev.kordex.core.utils.respond
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import qbtbot.model.Torrent
import qbtbot.util.BASE_URL
import qbtbot.util.bold
import qbtbot.util.httpClient
import qbtbot.util.requestQbitTorrentForLogin
import qbtbot.util.round
import qbtbot.util.testGuild

class TorrentInfo : Extension() {
    override val name: String = "Torrent Info"

    override suspend fun setup() {
        chatCommand {
            name = "torrents"
            description = "Get information of available torrents"
            check { failIf(event.message.author == null) }
            check { failIf(event.message.getGuild().id != testGuild) }
            action {
                var response = httpClient.get("$BASE_URL/torrents/info")
                if (response.status == HttpStatusCode.Forbidden) {
                    requestQbitTorrentForLogin()
                    // Try again single time after requesting login again in case of 403 due to timeout
                    response = httpClient.get("$BASE_URL/torrents/info").body()
                }
                val torrents: List<Torrent> = response.body()
                val finalMessage = torrents.groupBy({ it.category }, { it })
                    .map { (category, torrents) ->
                        "Category: ${category.bold()}\n${
                            torrents.joinToString(separator = "\n") {
                                "${it.name.substring(0, it.name.length.coerceAtMost(TORRENT_NAME_MAX_LENGTH))} is at ${(it.progress * PROGRESS_FACTOR).round()}% and ${it.state}"
                            }
                        }"
                    }.joinToString(separator = "\n\n\n")
                message.respond(finalMessage)
            }
        }
    }

    private companion object {
        private const val PROGRESS_FACTOR = 100
        private const val TORRENT_NAME_MAX_LENGTH = 50
    }
}
