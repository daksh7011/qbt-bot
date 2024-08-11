package qbtbot.commands

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.utils.respond
import io.ktor.client.call.body
import io.ktor.client.request.get
import qbtbot.model.Torrent
import qbtbot.util.BASE_URL
import qbtbot.util.bold
import qbtbot.util.httpClient
import qbtbot.util.round

class TorrentInfo : Extension() {
    override val name: String = "Torrent Info"
    override suspend fun setup() {
        chatCommand {
            name = "torrents"
            description = "Get information of available torrents"
            check { failIf(event.message.author == null) }
            action {
                val torrents: List<Torrent> = httpClient.get("$BASE_URL/torrents/info").body()
                val response = torrents.groupBy({ it.category }, { it })
                    .map { (category, torrents) ->
                        "Category: ${category.bold()}\n${
                            torrents.joinToString(separator = "\n") {
                                "${
                                    it.name.substring(
                                        0,
                                        it.name.length.coerceAtMost(50)
                                    )
                                } is ${(it.progress * 100).round()}%"
                            }
                        }"
                    }.joinToString(separator = "\n\n\n")
                message.respond(response)
            }
        }
    }
}
