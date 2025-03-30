package devcon.map.repository

import devcon.map.restapi.KeywordDocument
import devcon.map.restapi.KeywordSearchCallback
import devcon.map.restapi.KeywordSearchService
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val searchKeywordHolder = MutableStateFlow<List<KeywordDocument>>(emptyList())

    /** @param query string to query
     * @param page quantity of page **/
    fun searchKeyword(
        query: String,
        page: Int
    ) {
        retrofit.create(KeywordSearchService::class.java).run {
            for (i in 1..page) {
                searchKeyword(query, page = i)
                    .enqueue(KeywordSearchCallback(
                        callback = {
                            searchKeywordHolder.value =
                                (searchKeywordHolder.value + it)

                        }
                    )
                    )
            }
        }
    }
}