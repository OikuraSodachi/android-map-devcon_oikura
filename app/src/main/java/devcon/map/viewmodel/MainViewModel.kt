package devcon.map.viewmodel

import androidx.lifecycle.ViewModel
import devcon.map.repository.SampleRepository

class MainViewModel(private val sampleRepository: SampleRepository) : ViewModel() {

    val dataFlow = sampleRepository.dataFlow()
    val historyFlow = sampleRepository.historyFlow()

    fun onInit() {
        sampleRepository.initData()
    }
}