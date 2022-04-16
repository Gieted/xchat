package pl.pawelkielb.xchat

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


class Root : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.sendRedirect("/v1")
    }
}

class V1 : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html"
        resp.writer.use { 
            it.write("xchat API v1")
        }
    }
}

class V1Resource : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "application/json"
        resp.writer.use { 
            it.write("\"test6\"")
        }
    }
}
