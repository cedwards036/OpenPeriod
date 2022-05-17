package dev.christopheredwards.openperiod.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDate

@Dao
interface PeriodDao {

    @Insert
    suspend fun insertPDate(pDate: PDate): Long

    @Update
    suspend fun updatePDate(pDate: PDate): Int

    @Delete
    suspend fun deletePDate(pDate: PDate): Int

    @Query("SELECT * FROM p_date WHERE date = :date")
    suspend fun getPDateByDate(date: LocalDate): PDate?

    @Query("SELECT * FROM period")
    fun getAllPeriods(): LiveData<List<Period>>

    // TODO: consider whether to convert this returned result to LiveData<Boolean?>
    @Query(
        """
        SELECT period_started AND NOT period_ended
        FROM p_date
        WHERE date = (
           SELECT MAX(date)
           FROM p_date
           WHERE date <= :date
              AND (period_started OR period_ended)
        )
        """
    )
    suspend fun isOnPeriod(date: LocalDate): Boolean?
}