package devcon.map.viewmodel

import androidx.lifecycle.ViewModel
import devcon.map.repository.RetrofitRepository
import devcon.map.repository.SampleRepository
import devcon.map.restapi.KeywordDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sampleRepository: SampleRepository,
    private val retrofitRepository: RetrofitRepository
) : ViewModel() {

    /** sort 기준을 다듬을 필요가 있을듯? **/
    val historyFlow = sampleRepository.historyFlow()

    private val _contentFlow = MutableStateFlow<List<KeywordDocument>>(emptyList())
    val contentFlow: StateFlow<List<KeywordDocument>>
        get() = _contentFlow

    fun afterChanged(text: String) {
        retrofitRepository.searchKeyword(
            query = text,
            callback = { response ->
                response?.let {
                    _contentFlow.value = it.documents
                }
            }
        )
    }

    fun onDeleteHistory(placeName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sampleRepository.deleteHistory(placeName)
        }
    }

    fun onSelectItem(data: KeywordDocument) {
        CoroutineScope(Dispatchers.IO).launch {
            sampleRepository.insertHistory(data)
        }
    }
}