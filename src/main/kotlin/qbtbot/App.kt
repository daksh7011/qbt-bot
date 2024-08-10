package qbtbot

import com.kotlindiscord.kord.extensions.DISCORD_GREEN
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.commands.events.PublicSlashCommandFailedChecksEvent
import com.kotlindiscord.kord.extensions.commands.events.PublicSlashCommandFailedParsingEvent
import com.kotlindiscord.kord.extensions.commands.events.PublicSlashCommandFailedWithExceptionEvent
import com.kotlindiscord.kord.extensions.commands.events.PublicSlashCommandInvocationEvent
import com.kotlindiscord.kord.extensions.commands.events.PublicSlashCommandSucceededEvent
import com.kotlindiscord.kord.extensions.utils.env
import com.kotlindiscord.kord.extensions.utils.getKoin
import dev.kord.common.entity.PresenceStatus
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.gateway.builder.Shards
import qbtbot.commands.TorrentInfo
import qbtbot.util.Environment
import qbtbot.util.PresenceManager
import qbtbot.util.loginToBackend
import qbtbot.util.testGuild

// inv link: https://discord.com/oauth2/authorize?client_id=1271843928060203031&permissions=563364418284608&integration_type=0&scope=bot

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

