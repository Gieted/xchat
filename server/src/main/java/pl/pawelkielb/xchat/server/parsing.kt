package pl.pawelkielb.xchat.server

import pl.pawelkielb.xchat.data.Name
import java.time.Instant
import java.util.*

private fun cannotParse(parameterName: String, providedValue: String) =
    IllegalArgumentException("Cannot parse '$parameterName': $providedValue")

fun parsePage(string: String?) =
    if (string != null)
        runCatching { string.toInt() }.getOrElse { throw cannotParse("page", string) }
            .also {
                // validate it
                require(it >= 0) { "Page cannot be lower than 0, was $string" }
            }
    else null

fun parsePageSize(string: String?) =
    if (string != null)
        runCatching { string.toInt() }.getOrElse { throw cannotParse("pageSize", string) }.also {
            // validate it
            require(it in 1..100) { "Page size must be in a range <1, 100>, was $string" }
        }
    else null

fun parseChannel(string: String): UUID =
    runCatching { UUID.fromString(string) }.getOrElse { throw cannotParse("channel", string) }

fun parseInstant(parameterName: String, string: String?): Instant? =
    if (string != null)
        runCatching { Instant.ofEpochMilli(string.toLong()) }
            .getOrElse { throw cannotParse(parameterName, string) }
    else null

fun parseName(parameterName: String, string: String): Name =
    try {
        Name.of(string)
    } catch (e: IllegalArgumentException) {
        throw cannotParse(parameterName, string)
    }
