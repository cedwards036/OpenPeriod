package dev.christopheredwards.openperiod.data

import java.time.LocalDate

class PeriodRepository(private val periodDataSource: PeriodDao) {
    suspend fun insertPDate(pDate: PDate) = periodDataSource.insertPDate(pDate)
    suspend fun updatePDate(pDate: PDate) = periodDataSource.updatePDate(pDate)
    suspend fun deletePDate(pDate: PDate) = periodDataSource.deletePDate(pDate)
    suspend fun getPDateByDate(date: LocalDate) = periodDataSource.getPDateByDate(date)
    fun getAllPeriods() = periodDataSource.getAllPeriods()
    suspend fun isOnPeriod(date: LocalDate) = periodDataSource.isOnPeriod(date) ?: false
}