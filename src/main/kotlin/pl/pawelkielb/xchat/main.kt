package pl.pawelkielb.xchat

import pl.pawelkielb.xchat.dagger.AppComponent
import pl.pawelkielb.xchat.dagger.DaggerAppComponent
import java.lang.System.err
import java.lang.System.getenv
import kotlin.system.exitProcess


fun main() {
    val port = runCatching { getenv("PORT")?.toInt() }.getOrElse {
        err.println("There was an error while parsing PORT")
        exitProcess(1)
    }
    
    val component: AppComponent = DaggerAppComponent.create()
    val applicationScope = component.applicationScope()

    with(applicationScope) {
        val server = if (port != null) Server(port) else Server()
        server.start()
    }
}
