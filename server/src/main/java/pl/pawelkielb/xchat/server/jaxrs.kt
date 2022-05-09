package pl.pawelkielb.xchat.server

import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import pl.pawelkielb.xchat.data.Name


fun badRequestResponse(message: String?): Response =
    Response
        .status(Response.Status.BAD_REQUEST)
        .entity(message ?: "")
        .type(MediaType.TEXT_PLAIN)
        .build()

fun badRequest(message: String?) =
    WebApplicationException(
        badRequestResponse(message)
    )

fun getUserFromAuthorizationHeader(headers: HttpHeaders): Name {
    val authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION) ?: emptyList()

    if (authHeaders.isEmpty()) throw NotAuthorizedException("Authorization header missing")

    if (authHeaders.size != 1) {
        throw NotAuthorizedException("Multiple authorization headers not allowed")
    }

    val nameString = authHeaders.single()

    return try {
        parseName(parameterName = "Authorization header", string = nameString)
    } catch (e: IllegalArgumentException) {
        throw NotAuthorizedException("Authorization must be a valid name", e)
    }
}
