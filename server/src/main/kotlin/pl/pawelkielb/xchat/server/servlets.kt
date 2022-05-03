package pl.pawelkielb.xchat.server

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RootServlet @Inject constructor() : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.sendRedirect("/v1" + req.requestURI)
    }
}
