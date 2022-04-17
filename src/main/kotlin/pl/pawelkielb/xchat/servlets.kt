package pl.pawelkielb.xchat

import com.google.gson.Gson
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import javax.inject.Inject


class RootServlet @Inject constructor(): HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.sendRedirect("/v1")
    }
}

class V1Servlet @Inject constructor(): HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "text/html"
        resp.writer.use {
            it.write("xchat API v1")
        }
    }
}

class V1ResourceServlet @Inject constructor(
    private val resourceManager: ResourceManager,
    private val gson: Gson
) : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "application/json"
        val path = req.pathInfo.split("/").drop(1)

        // if path length is even
        val response = if (path.size % 2 != 0) {
            resourceManager.list(req.pathInfo)
        } else {
            null
        }

        resp.writer.use {
            it.write(gson.toJson(response))
        }
    }
}
