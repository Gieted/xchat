package pl.pawelkielb.xchat.model

import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import pl.pawelkielb.xchat.access_control.usersAccessRule
import pl.pawelkielb.xchat.UserData

data class User(val id: Id<UserData>, private val db: CoroutineDatabase) : Model {
    override val accessRule = usersAccessRule
    
    suspend fun getChannels(): Set<Channel> {
        TODO()
    }
}
