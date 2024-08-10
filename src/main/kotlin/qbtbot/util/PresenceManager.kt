package qbtbot.util

import com.kotlindiscord.kord.extensions.utils.scheduling.Scheduler
import dev.kord.core.Kord
import dev.kord.gateway.builder.PresenceBuilder
import kotlinx.coroutines.flow.count
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.floor

object PresenceManager : KoinComponent {

	private val scheduler = Scheduler()

	suspend fun setPresence(kord: Kord) {
		callScheduler(kord)
	}

	private suspend fun callScheduler(kord: Kord) {
		val listOfPresence = getPresenceList(kord)
		scheduler.schedule(
			60L,
			callback = {
				kord.editPresence(listOfPresence[floor(Math.random() * listOfPresence.size).toInt()])
				callScheduler(kord)
			},
			name = "Presence Task",
		)
	}

	private suspend fun getPresenceList(kord: Kord): List<PresenceBuilder.() -> Unit> {
		val listOfPresence = mutableListOf<PresenceBuilder.() -> Unit>()
		val totalServers = kord.guilds.count()
		var totalChannels = 0
		kord.guilds.collect {
			totalChannels += it.channels.count()
		}
		listOfPresence.add { watching("$totalServers servers") }
		listOfPresence.add { watching("$totalChannels channels") }
		listOfPresence.add { watching("torrent downloads") }
		listOfPresence.add { watching("torrent uploads") }
		listOfPresence.add { watching("torrent leechers") }
		listOfPresence.add { playing("with your heart") }
		listOfPresence.add { playing("in Slothie's CPU") }
		listOfPresence.add { playing("with my dark saber") }
		listOfPresence.add { playing("with Lord Moti") }
		listOfPresence.add { playing("/help") }
		return listOfPresence
	}

	fun cleanup(){
		scheduler.shutdown()
	}
}
