package devcon.map.repository

import devcon.map.restapi.KeywordSearchResponse
import devcon.map.restapi.KeywordSearchService
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitRepository {
    /*
    fun getSearchResult(keyword:String){
        KeywordSearchRetrofit.retrofit.create(KeywordSearchService::class.java)
            .getKeywordSearchResult().enqueue()
    }

     */

    fun searchKeyword(
        query: String,
        x: String? = null,
        y: String? = null,
        radius: Int? = null,
        rect: String? = null,
        callback: (KeywordSearchResponse?) -> Unit
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(KeywordSearchService::class.java)

        apiService.searchKeyword(query, x, y, radius, rect).enqueue(object : retrofit2.Callback<KeywordSearchResponse> {
            override fun onResponse(
                call: Call<KeywordSearchResponse>,
                response: Response<KeywordSearchResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    // API 호출 실패 처리
                    println("API 호출 실패: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<KeywordSearchResponse>, t: Throwable) {
                // 네트워크 오류 등 처리
                println("API 호출 실패: ${t.message}")
                callback(null)
            }
        })
    }
}