package pl.pawelkielb.xchat.server.dagger

import dagger.BindsInstance
import dagger.Component
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @Named("databaseRoot") databaseRoot: File): AppComponent
    }

    fun applicationContext(): ApplicationContext
}
