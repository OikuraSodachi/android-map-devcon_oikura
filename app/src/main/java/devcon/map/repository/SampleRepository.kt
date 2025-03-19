package devcon.map.repository

import android.content.Context
import devcon.map.data.MyDatabaseHelper
import devcon.map.data.SampleData

class SampleRepository(context: Context) {
    private val dbHelper = MyDatabaseHelper(context)

    fun initData() {
        if (readData().isEmpty()) {
            for (i in 1..200) {
                insertData(i.toLong(), "title$i")
            }
        }
    }

    fun insertData(id: Long, title: String): Long {
        return dbHelper.insertData(id, title)
    }

    fun readData(): List<SampleData> {
        return dbHelper.readData()
    }

    fun deleteData(id: Long): Int {
        return dbHelper.deleteData(id)
    }

    fun updateData(id: Long, title: String): Int {
        return dbHelper.updateData(id, title)
    }

}