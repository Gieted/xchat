package pl.pawelkielb.xchat.dagger

import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun gson() = Gson()
}
