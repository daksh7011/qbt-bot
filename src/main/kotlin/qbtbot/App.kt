package qbtbot

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.gateway.builder.Shards
import dev.kordex.core.DISCORD_GREEN
import dev.kordex.core.ExtensibleBot
import dev.kordex.core.commands.events.*
import dev.kordex.core.utils.env
import dev.kordex.core.utils.getKoin
import qbtbot.commands.TorrentInfo
import qbtbot.util.Environment
import qbtbot.util.PresenceManager
import qbtbot.util.loginToBackend
import qbtbot.util.testGuild

suspend fun main() {
    val bot = ExtensibleBot(env(Environment.TOKEN)) {
        chatCommands {
            defaultPrefix = env(Environment.PREFIX)
            enabled = true
            invokeOnMention = true

            prefix { default ->
                if (guildId == testGuild) {
                    // For the test server, we use ! as the command prefix
                    "!"
                } else {
                    // For other servers, we use the configured default prefix
                    default
                }
            }
        }

        extensions {
            help {
                pingInReply = true
                color { DISCORD_GREEN }
                deletePaginatorOnTimeout = true
                deleteInvocationOnPaginatorTimeout = true
            }

            add(::TorrentInfo)
        }

        hooks {
            // Custom koin modules
            afterKoinSetup {
                getKoin().loadModules(listOf())
            }
        }

        presence {
            status = PresenceStatus.Online
            playing("/help")
        }

        sharding { recommended ->
            Shards(recommended)
        }

        members {
            fillPresences = true
            all()
        }
    }

    bot.on<PublicSlashCommandInvocationEvent> {
        val commandName = this.command.name
        val userName = this.event.interaction.user.asUser().username
        val userDiscriminator = this.event.interaction.user.asUser().discriminator
        bot.logger.info { "Slash Command: $commandName triggered by $userName#$userDiscriminator" }
    }
    bot.on<PublicSlashCommandSucceededEvent> {
        bot.logger.info { "${this.command.name} was successfully executed." }
    }
    bot.on<PublicSlashCommandFailedChecksEvent> {
        val commandName = this.command.name
        bot.logger.info { "SlashCommand: $commandName failed because checks did not pass." }
    }
    bot.on<PublicSlashCommandFailedParsingEvent> {
        val commandName = this.command.name
        bot.logger.info { "SlashCommand: $commandName failed because there of a parsing issue." }
    }
    bot.on<PublicSlashCommandFailedWithExceptionEvent> {
        val commandName = this.command.name
        bot.logger.info { "SlashCommand: $commandName failed because there was an exception." }
        bot.logger.info { "More details about exception: ${this.throwable.localizedMessage}" }
        bot.logger.error { "Stacktrace: ${this.throwable.stackTraceToString()}" }
    }

    bot.on<ReadyEvent> {
        PresenceManager.setPresence(kord)
        bot.loginToBackend()
    }

    bot.on<DisconnectEvent> {
        PresenceManager.cleanup()
    }

    bot.start()
}
