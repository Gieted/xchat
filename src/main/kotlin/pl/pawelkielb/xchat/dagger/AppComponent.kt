package pl.pawelkielb.xchat.dagger

import dagger.Component
import pl.pawelkielb.xchat.ApplicationScope
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun applicationScope(): ApplicationScope
}
