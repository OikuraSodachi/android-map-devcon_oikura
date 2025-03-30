package devcon.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import devcon.map.repository.HistoryRepository
import devcon.map.repository.RetrofitRepository
import devcon.map.restapi.KeywordDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val historyRepository: HistoryRepository,
    private val retrofitRepository: RetrofitRepository
) : ViewModel() {

    /** sort 기준을 다듬을 필요가 있을듯? **/
    val historyFlow = historyRepository.historyFlow()

    val contentFlow: Flow<List<KeywordDocument>>
        get() = retrofitRepository.searchKeywordHolder

    fun afterChanged(text: String) {
        viewModelScope.launch {
            retrofitRepository.searchKeyword(
                query = text,
                page = 2
            )
        }
    }

    fun onDeleteHistory(placeName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            historyRepository.deleteHistory(placeName)
        }
    }

    fun onSelectItem(data: KeywordDocument) {
        CoroutineScope(Dispatchers.IO).launch {
            historyRepository.insertHistory(data)
        }
    }
}