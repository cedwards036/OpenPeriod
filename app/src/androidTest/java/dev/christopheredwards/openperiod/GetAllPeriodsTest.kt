package dev.christopheredwards.openperiod

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.data.Period
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class PeriodTest : PeriodRepositoryTest() {

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
    fun testPeriodWithNoEndAndNoLengths() {
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
    fun testPeriodWithStartAndEndButNoCycleEnd() {
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
    fun testTwoSequentialPeriods_SuchThatTheFirstPeriodHasACycleEndDate() {
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

    @Test
    @Throws(Exception::class)
    fun testASingleDayWithBothPeriodStartAndEnd() {
        insertDates(
            periodStartAndEndDate("2022-01-01"),
            periodStartDate("2022-02-01")
        )
        testAllPeriods {
            val expected = listOf(
                Period(
                    startDate = LocalDate.parse("2022-01-01"),
                    endDate = LocalDate.parse("2022-01-01"),
                    cycleEndDate = LocalDate.parse("2022-01-31"),
                    periodLength = 1,
                    cycleLength = 31
                ),
                Period(
                    startDate = LocalDate.parse("2022-02-01"),
                )
            )
            assertEquals(expected, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testTwoPeriodsWithTheSameEndDate() {
        insertDates(
            periodStartDate("2022-01-01"),
            periodStartDate("2022-01-05"),
            periodEndDate("2022-01-07")
        )
        testAllPeriods {
            val expected = listOf(
                Period(
                    startDate = LocalDate.parse("2022-01-01"),
                    endDate = LocalDate.parse("2022-01-07"),
                    cycleEndDate = LocalDate.parse("2022-01-04"),
                    periodLength = 7,
                    cycleLength = 4
                ),
                Period(
                    startDate = LocalDate.parse("2022-01-05"),
                    endDate = LocalDate.parse("2022-01-07"),
                    periodLength = 3
                ),
            )
            assertEquals(expected, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testTwoPeriodsWithTheNoEndDate() {
        insertDates(
            periodStartDate("2022-01-01"),
            periodStartDate("2022-01-05")
        )
        testAllPeriods {
            val expected = listOf(
                Period(
                    startDate = LocalDate.parse("2022-01-01"),
                    cycleEndDate = LocalDate.parse("2022-01-04"),
                    cycleLength = 4
                ),
                Period(
                    startDate = LocalDate.parse("2022-01-05")
                ),
            )
            assertEquals(expected, it)
        }
    }
}