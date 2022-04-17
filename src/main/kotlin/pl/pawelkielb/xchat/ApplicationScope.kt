package pl.pawelkielb.xchat

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationScope @Inject constructor(
    val rootServlet: RootServlet,
    val v1Servlet: V1Servlet,
    val v1ResourceServlet: V1ResourceServlet
)
