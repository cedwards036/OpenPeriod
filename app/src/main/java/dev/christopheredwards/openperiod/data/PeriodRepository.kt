package dev.christopheredwards.openperiod.data

import java.time.LocalDate

class PeriodRepository(private val periodDataSource: PDateDao) {
    suspend fun insert(pDate: PDate) = periodDataSource.insert(pDate)
    suspend fun update(pDate: PDate) = periodDataSource.update(pDate)
    suspend fun delete(pDate: PDate) = periodDataSource.delete(pDate)
    suspend fun getByDate(date: LocalDate) = periodDataSource.getByDate(date)
    suspend fun isOnPeriod(date: LocalDate) = periodDataSource.isOnPeriod(date) ?: false
}