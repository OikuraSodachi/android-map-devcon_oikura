package devcon.map.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [KeywordHistory::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun keywordHistoryDao(): KeywordHistoryDao

    companion object {
        private var instance: MyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabase {
            if (instance == null) {
                synchronized(MyDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "room_db",
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance!!
        }
    }
}