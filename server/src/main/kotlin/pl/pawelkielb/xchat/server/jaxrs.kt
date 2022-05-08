package pl.pawelkielb.xchat.server

import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import pl.pawelkielb.xchat.data.Name
import java.time.Instant
import java.util.*


fun badRequest(message: String?) =
    WebApplicationException(
        Response
            .status(Response.Status.BAD_REQUEST)
            .entity(message ?: "")
            .type(MediaType.TEXT_PLAIN)
            .build()
    )

fun cannotParseException(parameterName: String, providedValue: String) =
    badRequest("Cannot parse '$parameterName': $providedValue")

fun parsePage(page: String?) =
    if (page != null)
        runCatching { page.toInt() }.getOrElse { throw cannotParseException("page", page) }
            .also {
                // validate it
                require(it >= 0) { throw badRequest("Page cannot be lower than 0") }
            }
    else null

fun parsePageSize(pageSize: String?) =
    if (pageSize != null)
        runCatching { pageSize.toInt() }.getOrElse { throw cannotParseException("pageSize", pageSize) }.also {
            // validate it
            require(it in 1..100) { "Page size must be in a range <1, 100>, was $pageSize" }
        }
    else null

fun parseChannel(channel: String): UUID =
    runCatching { UUID.fromString(channel) }.getOrElse { throw cannotParseException("channel", channel) }

fun parseInstant(string: String?, parameterName: String): Instant? = if (string != null)
    runCatching { Instant.ofEpochMilli(string.toLong()) }
        .getOrElse { throw cannotParseException(parameterName, string) }
else null

fun parseUser(headers: HttpHeaders): Name {
    val authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION) ?: emptyList()

    if (authHeaders.isEmpty()) throw NotAuthorizedException("Authorization header missing")

    if (authHeaders.size != 1) {
        throw NotAuthorizedException("Multiple authorization headers not allowed")
    }

    val nameString = authHeaders.single()

    return try {
        Name.of(nameString)
    } catch (e: IllegalArgumentException) {
        throw NotAuthorizedException("Authorization must be a valid name", e)
    }
}
