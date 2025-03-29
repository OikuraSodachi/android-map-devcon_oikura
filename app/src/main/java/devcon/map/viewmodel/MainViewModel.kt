package devcon.map.viewmodel

import androidx.lifecycle.ViewModel
import devcon.map.data.SampleData
import devcon.map.repository.RetrofitRepository
import devcon.map.repository.SampleRepository
import devcon.map.restapi.KeywordDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val sampleRepository: SampleRepository,  private val retrofitRepository : RetrofitRepository) : ViewModel() {

    /** sort 기준을 다듬을 필요가 있을듯? **/
    val historyFlow = sampleRepository.historyFlow()

    /*
    val contentFlowOriginal: Flow<List<SampleData>> = combine(
        sampleRepository.dataFlow(),
        searchFlow
    ) { data, search ->
        matchDataFilter(data, search)
    }
     */

    val _contentFlow = MutableStateFlow<List<KeywordDocument>>(emptyList())
    val contentFlow: StateFlow<List<KeywordDocument>>
        get() = _contentFlow

    fun afterChanged(text: String) {
        retrofitRepository.searchKeyword(
            query = text,
            callback = { response ->
                response?.let{
                    _contentFlow.value = it.documents
                }
            }
        )
    }

    /** db 초기 값 setter **/
    fun checkDB() {
        CoroutineScope(Dispatchers.IO).launch {
            sampleRepository.initData()
        }
    }

    fun onDeleteHistory(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            sampleRepository.deleteHistory(id)
        }
    }

    fun onSelectItem(data: KeywordDocument) {
        /*
        CoroutineScope(Dispatchers.IO).launch {
            sampleRepository.insertHistory(data)
        }
         */
    }

    /** 검색 데이터 필터링 **/
    private fun matchDataFilter(data: List<SampleData>, search: String): List<SampleData> {
        if (search.isEmpty()) {
            return data
        } else {
            return data.filter {
                it.title.contains(search)
            }
        }
    }
}