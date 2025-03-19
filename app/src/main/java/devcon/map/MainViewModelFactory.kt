package devcon.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import devcon.map.repository.SampleRepository
import devcon.map.viewmodel.MainViewModel

class MainViewModelFactory(private val myRepository: SampleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(myRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}