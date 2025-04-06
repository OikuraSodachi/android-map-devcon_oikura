package devcon.map.repository

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import com.kakao.vectormap.LatLng
import devcon.map.abstracts.MyDataStore

/** Todo: 백그라운드 상태에서 돌아오면 "There are multiple DataStores active" 에러 발생
 *  Todo: 로직을 Room 으로 옮기거나 dagger 사용 **/
class DataStoreRepository(context: Context) : MyDataStore(context) {

    companion object {
        private val DATASTORE_X = doublePreferencesKey("datastore_x")
        private val DATASTORE_Y = doublePreferencesKey("datastore_y")
        private val defaultX = 126.978652258823
        private val defaultY = 37.56682420267543
    }

    private suspend fun saveX(value: Double) = DATASTORE_X.save(value)
    suspend fun getX() = DATASTORE_X.notNullValue(defaultX)

    private suspend fun saveY(value: Double) = DATASTORE_Y.save(value)
    suspend fun getY() = DATASTORE_Y.notNullValue(defaultY)

    suspend fun saveLocation(latLng: LatLng) {
        saveX(latLng.longitude)
        saveY(latLng.latitude)
    }

    suspend fun getLocation(): LatLng {
        return LatLng.from(getY(), getX())
    }
}