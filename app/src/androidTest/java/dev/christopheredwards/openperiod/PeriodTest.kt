package dev.christopheredwards.openperiod

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.data.PDate
import dev.christopheredwards.openperiod.data.Period
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class PeriodTest : PeriodRepositoryTest() {

    private fun periodStartDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = true,
            periodEnded = false
        )
    }

    private fun periodEndDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = false,
            periodEnded = true
        )
    }


    private fun periodStartAndEndDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = true,
            periodEnded = true
        )
    }


    private fun noEventDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = false,
            periodEnded = false
        )
    }

    private fun insertDates(vararg dates: PDate) {
        runBlocking {
            for (date in dates) {
                repository.insertPDate(date)
            }
        }
    }

    private fun testAllPeriods(testCode: (periods: List<Period>) -> Unit) {
        repository.getAllPeriods().observeForever {
            testCode(it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun returnsNull_ifNoDateDataHasBeenInserted() {
        testAllPeriods {
            assertEquals(0, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun returnsNull_ifNoDatesHavePeriodEventFlags() {
        insertDates(noEventDate("2022-01-01"))
        testAllPeriods {
            assertEquals(0, it.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodWithNoEndAndNoEstimatesAndNoLengths() {
        insertDates(periodStartDate("2022-01-01"))
        testAllPeriods {
            val expected = listOf(
                Period(
                    startDate = LocalDate.parse("2022-01-01")
                )
            )
            assertEquals(expected, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodWithStartAndEndButNoEstimates() {
        insertDates(
            periodStartDate("2022-01-01"),
            periodEndDate("2022-01-06")
        )
        testAllPeriods {
            val expected = listOf(
                Period(
                    startDate = LocalDate.parse("2022-01-01"),
                    endDate = LocalDate.parse("2022-01-06"),
                    periodLength = 6
                )
            )
            assertEquals(expected, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testTwoPeriods_SuchThatTheFirstPeriodHasACycleEndDate() {
        insertDates(
            periodStartDate("2022-01-01"),
            periodEndDate("2022-01-06"),
            periodStartDate("2022-02-02")
        )
        testAllPeriods {
            val expected = listOf(
                Period(
                    startDate = LocalDate.parse("2022-01-01"),
                    endDate = LocalDate.parse("2022-01-06"),
                    cycleEndDate = LocalDate.parse("2022-02-01"),
                    periodLength = 6,
                    cycleLength = 32
                ),
                Period(
                    startDate = LocalDate.parse("2022-02-02"),
                )
            )
            assertEquals(expected, it)
        }
    }
}