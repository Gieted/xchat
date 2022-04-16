package pl.pawelkielb.xchat

import org.eclipse.jetty.server.Server as Jetty
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

class Server(private val port: Int = 8080) {
    private var jetty: Jetty? = null

    fun start() {
        val jetty = Jetty(port)
        this.jetty = jetty
        jetty.handler = ServletContextHandler().apply {
            addServlet(ServletHolder(Root()), "/")
            addServlet(ServletHolder(V1()), "/v1")
            addServlet(ServletHolder(V1Resource()), "/v1/*")
        }

        jetty.start()
    }

    fun stop() {
        jetty?.stop()
    }
}
