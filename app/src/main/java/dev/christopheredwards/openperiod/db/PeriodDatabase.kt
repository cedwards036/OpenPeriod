package dev.christopheredwards.openperiod.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Period::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PeriodDatabase : RoomDatabase() {

    abstract val periodDao: PeriodDao

    companion object {

        @Volatile
        private var INSTANCE: PeriodDatabase? = null

        fun getInstance(context: Context): PeriodDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PeriodDatabase::class.java,
                        "period_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}