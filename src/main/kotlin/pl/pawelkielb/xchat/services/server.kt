package pl.pawelkielb.xchat.services

import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import pl.pawelkielb.xchat.dagger.AppComponent
import pl.pawelkielb.xchat.dagger.DaggerAppComponent
import org.eclipse.jetty.server.Server as Jetty

class Server(
    port: Int = defaultPort,
    v1Servlet: V1Servlet,
    v1ResourceServlet: V1ResourceServlet,
) {
    companion object {
        const val defaultPort = 8080
    }
    
    private val jetty = Jetty(port).also {
        it.handler = ServletContextHandler().apply {
            addServlet(ServletHolder(RedirectServlet(redirectTo = "/v1")), "/")
            addServlet(ServletHolder(v1Servlet), "/v1")
            addServlet(ServletHolder(v1ResourceServlet), "/v1/*")
        }
    }

    fun start() {
        jetty.start()
    }

    fun stop() {
        jetty.stop()
    }
}

@JvmOverloads
fun Server(port: Int = Server.defaultPort): Server {
    val component: AppComponent = DaggerAppComponent.create()
    val applicationScope = component.applicationScope()
    with(applicationScope) { 
        return Server(port)
    }
}
