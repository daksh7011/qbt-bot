package qbtbot.util

import dev.kordex.core.utils.env
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.net.URLEncoder

const val API_VERSION = "v2"
val BASE_URL get() = "${env(Environment.BASE_URL)}/api/$API_VERSION"

val httpClient = HttpClient {
    install(HttpCookies)
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
        )
    }
}

fun String.encodeQuery(): String = URLEncoder.encode(this, "utf-8")

suspend fun getSessionCookie(): Cookie? = httpClient.cookies(BASE_URL).find { cookie -> cookie.name == "SID" }
