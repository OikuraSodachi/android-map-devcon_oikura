package devcon.map.repository

import android.content.Context
import androidx.datastore.preferences.core.floatPreferencesKey
import devcon.map.abstracts.MyDataStore

class DataStoreRepository(context: Context):MyDataStore(context) {

    companion object{
        private val DATASTORE_X = floatPreferencesKey("datastore_x")
        private val DATASTORE_Y = floatPreferencesKey("datastore_y")
        private val defaultX = 0f
        private val defaultY = 0f
    }

    suspend fun saveX(value:Float) = DATASTORE_X.save(value)
    suspend fun getX() = DATASTORE_X.notNullValue(defaultX)
    val xFlow = DATASTORE_X.flow()

    suspend fun saveY(value:Float) = DATASTORE_Y.save(value)
    suspend fun getY() = DATASTORE_Y.notNullValue(defaultY)
    val yFlow = DATASTORE_Y.flow()

}