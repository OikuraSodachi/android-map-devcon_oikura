package devcon.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import devcon.map.repository.RetrofitRepository
import devcon.map.repository.SampleRepository
import devcon.map.viewmodel.MainViewModel

class MainViewModelFactory(private val sampleRepository: SampleRepository, private val retrofitRepository : RetrofitRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(sampleRepository, retrofitRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}