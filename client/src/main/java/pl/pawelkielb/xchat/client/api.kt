package pl.pawelkielb.xchat.client

suspend fun <T : Any> fetchAll(
    generate: suspend (page: Int, pageSize: Int) -> List<T>,
    consume: suspend (List<T>) -> Unit
) {
    val maxPageSize = 100
    var page = 0
    while (true) {
        val results = generate(page, maxPageSize)
        consume(results)

        if (results.size < maxPageSize) {
            break
        }
        
        page++
    }
}
