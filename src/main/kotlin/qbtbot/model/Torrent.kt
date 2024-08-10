package qbtbot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Torrent(
    @SerialName("added_on")
    val addedOn: Int,
    @SerialName("amount_left")
    val amountLeft: Double,
    @SerialName("auto_tmm")
    val autoTmm: Boolean,
    @SerialName("availability")
    val availability: Double,
    @SerialName("category")
    val category: String,
    @SerialName("completed")
    val completed: Long,
    @SerialName("completion_on")
    val completionOn: Int,
    @SerialName("content_path")
    val contentPath: String,
    @SerialName("dl_limit")
    val dlLimit: Int,
    @SerialName("dlspeed")
    val downSpeed: Int,
    @SerialName("download_path")
    val downloadPath: String,
    @SerialName("downloaded")
    val downloaded: Long,
    @SerialName("downloaded_session")
    val downloadedSession: Int,
    @SerialName("eta")
    val eta: Double,
    @SerialName("f_l_piece_prio")
    val firstLastPiecePriority: Boolean,
    @SerialName("force_start")
    val forceStart: Boolean,
    @SerialName("hash")
    val hash: String,
    @SerialName("inactive_seeding_time_limit")
    val inactiveSeedingTimeLimit: Int,
    @SerialName("infohash_v1")
    val infoHashV1: String,
    @SerialName("infohash_v2")
    val infoHashV2: String,
    @SerialName("last_activity")
    val lastActivity: Int,
    @SerialName("magnet_uri")
    val magnetUri: String,
    @SerialName("max_inactive_seeding_time")
    val maxInactiveSeedingTime: Int,
    @SerialName("max_ratio")
    val maxRatio: Int,
    @SerialName("max_seeding_time")
    val maxSeedingTime: Int,
    @SerialName("name")
    val name: String,
    @SerialName("num_complete")
    val numComplete: Int,
    @SerialName("num_incomplete")
    val numIncomplete: Int,
    @SerialName("num_leechs")
    val numLeech: Int,
    @SerialName("num_seeds")
    val numSeed: Int,
    @SerialName("priority")
    val priority: Int,
    @SerialName("progress")
    val progress: Double,
    @SerialName("ratio")
    val ratio: Double,
    @SerialName("ratio_limit")
    val ratioLimit: Int,
    @SerialName("save_path")
    val savePath: String,
    @SerialName("seeding_time")
    val seedingTime: Double,
    @SerialName("seeding_time_limit")
    val seedingTimeLimit: Int,
    @SerialName("seen_complete")
    val seenComplete: Int,
    @SerialName("seq_dl")
    val sequentialDownload: Boolean,
    @SerialName("size")
    val size: Double,
    @SerialName("state")
    val state: String,
    @SerialName("super_seeding")
    val superSeeding: Boolean,
    @SerialName("tags")
    val tags: String,
    @SerialName("time_active")
    val timeActive: Int,
    @SerialName("total_size")
    val totalSize: Double,
    @SerialName("tracker")
    val tracker: String,
    @SerialName("trackers_count")
    val trackersCount: Int,
    @SerialName("up_limit")
    val upLimit: Int,
    @SerialName("uploaded")
    val uploaded: Long,
    @SerialName("uploaded_session")
    val uploadedSession: Int,
    @SerialName("upspeed")
    val upSpeed: Int
)
