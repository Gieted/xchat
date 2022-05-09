package pl.pawelkielb.xchat.server.dagger

import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import pl.pawelkielb.xchat.Logger
import pl.pawelkielb.xchat.server.ConsoleLogger
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executor
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class AppModule {
    companion object {
        @Provides
        @Singleton
        fun db(): CoroutineDatabase {
            return KMongo.createClient().coroutine.getDatabase("xchat")
        }

        @Provides
        @Singleton
        fun coroutineScope() = CoroutineScope(Dispatchers.Default)

        @Provides
        @Singleton
        @Named("database")
        fun databaseRoot(): Path = Paths.get(".")

        @Provides
        @Named("io")
        @Singleton
        fun ioThreads(coroutineScope: CoroutineScope) = Executor { command ->
            coroutineScope.launch(Dispatchers.IO) {
                command.run()
            }
        }
    }

    @Binds
    abstract fun logger(to: ConsoleLogger): Logger
}
