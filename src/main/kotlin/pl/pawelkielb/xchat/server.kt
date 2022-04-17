package pl.pawelkielb.xchat

import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import pl.pawelkielb.xchat.dagger.AppComponent
import pl.pawelkielb.xchat.dagger.DaggerAppComponent
import org.eclipse.jetty.server.Server as Jetty

private const val defaultPort = 8080

class Server(
    port: Int = defaultPort,
    rootServlet: RootServlet,
    v1Servlet: V1Servlet,
    v1ResourceServlet: V1ResourceServlet,
) {
    private val jetty = Jetty(port).also {
        it.handler = ServletContextHandler().apply {
            addServlet(ServletHolder(rootServlet), "/")
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

fun ApplicationScope.Server(port: Int = defaultPort) = Server(port, rootServlet, v1Servlet, v1ResourceServlet)

fun Server(): Server {
    val component: AppComponent = DaggerAppComponent.create()
    val applicationScope = component.applicationScope()
    with(applicationScope) { 
        return Server()
    }
}
