package dev.christopheredwards.openperiod.db

import androidx.room.*
import java.time.LocalDate

@Dao
interface PDateDao {

    @Insert
    fun insert(pDate: PDate): Long

    @Update
    fun update(pDate: PDate): Int

    @Delete
    fun delete(pDate: PDate): Int

    @Query("SELECT * FROM p_date WHERE date = :date")
    fun getByDate(date: LocalDate): PDate?
}