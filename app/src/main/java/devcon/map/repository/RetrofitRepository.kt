package devcon.map.repository

import devcon.map.restapi.KeywordDocument
import devcon.map.restapi.KeywordSearchResponse
import devcon.map.restapi.KeywordSearchService
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val searchKeywordHolder = MutableStateFlow<List<KeywordDocument>>(emptyList())

    fun searchKeyword(
        query: String,
        page: Int
    ) {
        val apiService = retrofit.create(KeywordSearchService::class.java)
        apiService.run {
            for (i in 1..page) {
                searchKeyword(query, page = i)
                    .enqueue(
                        object : retrofit2.Callback<KeywordSearchResponse> {
                            override fun onResponse(
                                call: Call<KeywordSearchResponse>,
                                response: Response<KeywordSearchResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val doc = response.body()?.documents ?: emptyList()
                                    searchKeywordHolder.value =
                                        (searchKeywordHolder.value + doc)
                                } else {
                                    // API 호출 실패 처리
                                    println("API 호출 실패: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(
                                call: Call<KeywordSearchResponse>,
                                t: Throwable
                            ) {
                                // 네트워크 오류 등 처리
                                println("API 호출 실패: ${t.message}")
                                //callback(null)
                            }
                        }
                    )
            }
        }
    }
}