package devcon.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.LatLng
import devcon.map.repository.DataStoreRepository
import devcon.map.repository.HistoryRepository
import devcon.map.repository.RetrofitRepository
import devcon.map.restapi.KeywordDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitRepository: RetrofitRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    /** 쿼리 넣을 페이지 수 ( keywordDocument 의 최대 갯수 == 15 * searchQuantity )**/
    private val searchQuantity = 2

    /** sort 기준을 다듬을 필요가 있을듯? **/
    val historyFlow = historyRepository.historyFlow()

    suspend fun getStartPoint() = dataStoreRepository.getLocation()

    private val _contentFlow = MutableStateFlow<List<KeywordDocument>>(emptyList())
    val contentFlow: Flow<List<KeywordDocument>>
        get() = _contentFlow

    fun afterChanged(text: String) {
        viewModelScope.launch {
            _contentFlow.value =
                retrofitRepository.searchKeyword(
                    query = text,
                    page = searchQuantity
                )
        }
    }

    fun onDeleteHistory(data: KeywordDocument) {
        CoroutineScope(Dispatchers.IO).launch {
            historyRepository.deleteHistory(data.place_name, data.address_name)
        }
    }

    fun saveHistory(data: KeywordDocument) {
        CoroutineScope(Dispatchers.IO).launch {
            historyRepository.insertHistory(data)
        }
    }

    fun saveLastLocation(coordinate: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            println("save: ${coordinate.longitude}, ${coordinate.latitude}")
            dataStoreRepository.saveLocation(coordinate)
        }
    }
}