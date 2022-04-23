package pl.pawelkielb.xchat.data

import org.litote.kmongo.coroutine.CoroutineDatabase
import javax.inject.Inject

data class ResourceManagerContext @Inject constructor(val db: CoroutineDatabase)
