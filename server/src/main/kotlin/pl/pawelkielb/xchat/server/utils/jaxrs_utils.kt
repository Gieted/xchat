package pl.pawelkielb.xchat.server.utils

import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response


fun badRequest(message: String?) =
    WebApplicationException(
        Response
            .status(Response.Status.BAD_REQUEST)
            .entity(message ?: "")
            .type(MediaType.TEXT_PLAIN)
            .build()
    )
