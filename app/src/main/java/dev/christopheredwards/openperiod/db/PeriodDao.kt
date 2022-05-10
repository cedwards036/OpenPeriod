package dev.christopheredwards.openperiod.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PeriodDao {
    @Insert
    fun insert(period: Period)

    @Query("SELECT 1 FROM period WHERE end_date IS NULL")
    fun isOnPeriod(): Boolean
}