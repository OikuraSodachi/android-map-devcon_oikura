package devcon.map.repository

import devcon.map.data.room.KeywordHistory
import devcon.map.data.room.KeywordHistoryDao
import devcon.map.restapi.KeywordDocument
import kotlinx.coroutines.flow.map

class HistoryRepository(private val historyDao: KeywordHistoryDao) {

    fun historyFlow() = historyDao.getAll().map {
        it.map{
            it.toKeywordDocument()
        }
    }

    suspend fun insertHistory(data: KeywordDocument){
        historyDao.insert(data.toKeywordHistory())
    }

    suspend fun deleteHistory(placeName:String,addressName:String){
        historyDao.delete(placeName,addressName)
    }

    private fun KeywordDocument.toKeywordHistory() = KeywordHistory(
        place_name = place_name,
        address_name = address_name,
        road_address_name = road_address_name,
        x = x,
        y = y
    )
}