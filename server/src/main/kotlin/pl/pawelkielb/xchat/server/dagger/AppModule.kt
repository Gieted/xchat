package pl.pawelkielb.xchat.server.dagger

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.concurrent.Executor
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun db(): CoroutineDatabase {
        return KMongo.createClient().coroutine.getDatabase("xchat")
    }

    @Provides
    @Singleton
    fun coroutineScope() = CoroutineScope(Dispatchers.Default)

    @Provides
    @Named("io")
    fun ioThreads(coroutineScope: CoroutineScope) = Executor { command ->
        coroutineScope.launch(Dispatchers.IO) {
            command.run()
        }
    }
}
