package devcon.map.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import devcon.map.restapi.KeywordDocument

/** Todo: place_name 값이 중복되는 케이스 대응 필요 **/
@Entity(tableName = "room_keyword_history")
data class KeywordHistory(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo val place_name: String,
    @ColumnInfo val address_name: String,
    @ColumnInfo val road_address_name: String,
    @ColumnInfo val x : String,
    @ColumnInfo val y : String
){
    fun toKeywordDocument() = KeywordDocument(
        place_name = place_name,
        address_name = address_name,
        road_address_name = road_address_name,
        x = x,
        y = y
    )
}
