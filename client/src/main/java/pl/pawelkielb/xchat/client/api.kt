package pl.pawelkielb.xchat.client

import pl.pawelkielb.xchat.maxPageSize

suspend fun <T : Any> fetchAll(
    generate: suspend (page: Int, pageSize: Int) -> List<T>,
    consume: suspend (List<T>) -> Unit
) {
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
