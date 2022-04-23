package pl.pawelkielb.xchat.dagger

import dagger.Component
import pl.pawelkielb.xchat.data.ApplicationContext
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun applicationScope(): ApplicationContext
}
