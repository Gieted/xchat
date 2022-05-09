package pl.pawelkielb.xchat.server

import jakarta.ws.rs.ext.ExceptionMapper

class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {
    override fun toResponse(exception: IllegalArgumentException) = badRequestResponse(exception.message)
}
