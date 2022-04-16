package pl.pawelkielb.xchat.dev

import kotlinx.coroutines.coroutineScope
import java.io.File
import java.net.URLClassLoader
import kotlin.concurrent.timer

suspend fun main(): Unit = coroutineScope {
    var server: Any? = null
    var serverClass: Class<*>? = null
    var lastWatch = "null"

    timer(period = 100) {
        val currentWatch = File("build/watch").readText()
        if (currentWatch != lastWatch) {
            lastWatch = currentWatch

            // restart server
            if (server != null) {
                println("Restarting the server...")
                // server.stop()
                serverClass!!.declaredMethods.find { it.name == "stop" }!!.invoke(server)
            }
            
            val classLoader = URLClassLoader(arrayOf(File("build/classes/kotlin/main").toURI().toURL()))
            serverClass = classLoader.loadClass("pl.pawelkielb.xchat.Server")
            
            server = serverClass!!
                .declaredConstructors
                .find { it.parameterCount == 0 }!!
                .newInstance()

            // server.start()
            serverClass!!.declaredMethods.find { it.name == "start" }!!.invoke(server)
        }
    }
}
