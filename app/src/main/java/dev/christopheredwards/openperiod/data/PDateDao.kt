package dev.christopheredwards.openperiod.data

import androidx.room.*
import java.time.LocalDate

@Dao
interface PDateDao {

    @Insert
    suspend fun insert(pDate: PDate): Long

    @Update
    suspend fun update(pDate: PDate): Int

    @Delete
    suspend fun delete(pDate: PDate): Int

    @Query("SELECT * FROM p_date WHERE date = :date")
    suspend fun getByDate(date: LocalDate): PDate?

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