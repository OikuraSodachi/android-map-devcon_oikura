package devcon.map.restapi

import retrofit2.Call
import retrofit2.Response

class KeywordSearchCallback(private val callback:(t:List<KeywordDocument>)->Unit):retrofit2.Callback<KeywordSearchResponse> {
    override fun onResponse(
        call: Call<KeywordSearchResponse>,
        response: Response<KeywordSearchResponse>
    ) {
        if (response.isSuccessful) {
            callback(response.body()?.documents ?: emptyList())
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
    }
}