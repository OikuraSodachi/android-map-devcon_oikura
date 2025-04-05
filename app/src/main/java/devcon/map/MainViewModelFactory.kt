package devcon.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import devcon.map.repository.DataStoreRepository
import devcon.map.repository.HistoryRepository
import devcon.map.repository.RetrofitRepository
import devcon.map.viewmodel.MainViewModel

class MainViewModelFactory(
    private val dataStoreRepository: DataStoreRepository,
    private val retrofitRepository: RetrofitRepository,
    private val historyRepository:HistoryRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataStoreRepository,retrofitRepository,historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}