package devcon.map.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface KeywordHistoryDao {
    @Query("SELECT * FROM room_keyword_history")
    fun getAll(): Flow<List<KeywordHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keywordHistory: KeywordHistory)

    @Query("delete from room_keyword_history where place_name = :placeName and address_name = :addressName")
    suspend fun delete(placeName:String,addressName:String)

}