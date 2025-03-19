package devcon.map.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "mytable"
        const val COLUMN_TITLE = "title"
        const val COLUMN_ID = "id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN new_column TEXT")
        }
    }

    fun insertData(id: Long, title: String): Long {
        return writableDatabase.insert(TABLE_NAME, null, values(id, title))
    }

    fun readData(): List<SampleData> {
        val dataList = mutableListOf<SampleData>()
        val cursor: Cursor = readableDatabase.query(TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))

                dataList.add(SampleData(id, title))
            }
        }

        cursor.close()
        return dataList
    }

    fun updateData(id: Long, title: String): Int {
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        return writableDatabase.update(TABLE_NAME, values(id, title), selection, selectionArgs)
    }

    fun deleteData(id: Long): Int {
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        return writableDatabase.delete(TABLE_NAME, selection, selectionArgs)
    }

    private fun values(id: Long, title: String): ContentValues {
        return ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_TITLE, title)
        }
    }
}