package pl.pawelkielb.xchat.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun applicationScope(): ApplicationContext
}
