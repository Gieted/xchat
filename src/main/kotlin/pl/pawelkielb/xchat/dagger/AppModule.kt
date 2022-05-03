package pl.pawelkielb.xchat.dagger

import dagger.Module
import dagger.Provides
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun db() = KMongo.createClient().coroutine.getDatabase("xchat")
}
