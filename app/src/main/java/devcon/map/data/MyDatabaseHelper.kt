package devcon.map.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import devcon.map.restapi.KeywordDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        const val HISTORY_TABLE_NAME = "history"

        const val PLACE_NAME = "place_name"
        const val ADDRESS_NAME = "address_name"
        const val ROAD_ADDRESS_NAME = "road_address_name"
        const val X_COORDINATE = "x"
        const val Y_COORDINATE = "y"
        const val DISTANCE = "distance"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createHistoryTableQuery = """
            CREATE TABLE $HISTORY_TABLE_NAME (
                $PLACE_NAME TEXT,
                $ADDRESS_NAME TEXT,
                $ROAD_ADDRESS_NAME TEXT,
                $X_COORDINATE TEXT,
                $Y_COORDINATE TEXT,
                $DISTANCE TEXT
            )
        """.trimIndent()

        db.execSQL(createHistoryTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $HISTORY_TABLE_NAME ADD COLUMN new_column TEXT")
        }
    }

    /** 중복 값 체크 안한 상태로 냅둬도 무방하려나...? **/
    private fun insert(data: KeywordDocument, table: String): Long {
        val result = writableDatabase.insert(table, null, values(data))
        notifyDataChanged()
        return result
    }

    private fun read(table: String): List<KeywordDocument> {
        val dataList = mutableListOf<KeywordDocument>()
        val cursor: Cursor = readableDatabase.query(table, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val placeName = getString(getColumnIndexOrThrow(PLACE_NAME))
                val addressName = getString(getColumnIndexOrThrow(ADDRESS_NAME))
                val roadAddressName = getString(getColumnIndexOrThrow(ROAD_ADDRESS_NAME))
                val xCoordinate = getString(getColumnIndexOrThrow(X_COORDINATE))
                val yCoordinate = getString(getColumnIndexOrThrow(Y_COORDINATE))
                val distance = getString(getColumnIndexOrThrow(DISTANCE))

                dataList.add(
                    KeywordDocument(
                        placeName,
                        addressName,
                        roadAddressName,
                        xCoordinate,
                        yCoordinate,
                        distance
                    )
                )
            }
        }

        cursor.close()
        return dataList
    }

    private fun update(data: KeywordDocument, table: String): Int {
        val result = writableDatabase.update(
            table,
            values(data),
            "$PLACE_NAME = ?",
            selectionArgs(data.place_name)
        )
        notifyDataChanged()
        return result
    }

    private fun delete(placeName: String, table: String): Int {
        val result = writableDatabase.delete(table, "$PLACE_NAME = ?", selectionArgs(placeName))
        notifyDataChanged()
        return result
    }

    private fun values(data: KeywordDocument): ContentValues {
        return ContentValues().apply {
            put(PLACE_NAME, data.place_name)
            put(ADDRESS_NAME, data.address_name)
            put(ROAD_ADDRESS_NAME, data.road_address_name)
            put(X_COORDINATE, data.x)
            put(Y_COORDINATE, data.y)
            put(DISTANCE, data.distance)
        }
    }

    private fun selectionArgs(placeName: String): Array<String> {
        return arrayOf(placeName)
    }

    fun insertHistory(data: KeywordDocument) = insert(data, HISTORY_TABLE_NAME)

    fun readHistory() = read(HISTORY_TABLE_NAME)

    fun updateHistory(data: KeywordDocument) = update(data, HISTORY_TABLE_NAME)

    fun deleteHistory(placeName: String) = delete(placeName, HISTORY_TABLE_NAME)

    //----------- Flow part -----------

    // A simple flag to indicate that data has changed
    private var dataChanged = false

    // Method to notify that data has changed
    @Synchronized
    private fun notifyDataChanged() {
        dataChanged = true
    }

    private fun getAsFlow(table: String): Flow<List<KeywordDocument>> = callbackFlow {
        send(read(table))

        while (isActive) {
            if (dataChanged) {
                dataChanged = false
                send(read(table))
            }
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    fun getHistoryAsFlow() = getAsFlow(HISTORY_TABLE_NAME)
}