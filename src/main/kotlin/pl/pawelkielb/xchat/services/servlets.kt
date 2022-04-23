package pl.pawelkielb.xchat.services

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import org.eclipse.jetty.http.HttpStatus
import pl.pawelkielb.xchat.Key
import pl.pawelkielb.xchat.access_control.Participant
import pl.pawelkielb.xchat.data.ChannelData
import pl.pawelkielb.xchat.json
import javax.inject.Inject
import javax.inject.Singleton


class RedirectServlet(private val redirectTo: String) : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.sendRedirect(redirectTo)
    }
}

@Singleton
class V1Servlet @Inject constructor() : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) = trimTrailingSlash(req, resp) ?: run {
        resp.contentType = "text/html"
        resp.writer.use {
            it.write("xchat API v1")
        }
    }
}

@Singleton
class V1ResourceServlet @Inject constructor(
    private val resourceManager: ResourceManager
) : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) =
        trimTrailingSlash(req, resp) ?: runBlocking {
            val key = req.pathInfo
            val participant = Participant(user = null)

            val response = try {
                if (Key.targetsGroup(key)) {
                    resourceManager.list(key, participant)
                } else {
                    null
                }
            } catch (e: Exception) {
                handleException(resp, key, e)
            }
            
            @Suppress("UNCHECKED_CAST")
            val responseString = when (req.pathInfo) {
                "/channels" -> json.encodeToString(response as List<ChannelData>?)
                else -> TODO()
            }

            resp.contentType = "application/json"
            resp.writer.use {
                it.write(responseString)
            }
        }
}

private fun handleException(resp: HttpServletResponse, key: String, exception: Exception) {
    when (exception) {
        is NoSuchElementException -> {
            resp.status = HttpStatus.NOT_FOUND_404
            resp.contentType = "text/html"
            resp.writer.use {
                it.write("Cannot find resource $key")
            }
        }
        else -> throw exception
    }
}

private fun trimTrailingSlash(req: HttpServletRequest, resp: HttpServletResponse) =
    if (req.requestURI.endsWith("/")) {
        resp.sendRedirect(req.requestURI.removeSuffix("/"))
    } else {
        null
    }
