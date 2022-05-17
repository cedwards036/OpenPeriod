package dev.christopheredwards.openperiod

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.data.Period
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class GetMostRecentPeriodTest : PeriodRepositoryTest() {

    private fun assertMostRecentPeriodIs(date: String, expectedPeriod: Period?) {
        repository.getMostRecentPeriod(LocalDate.parse(date)).observeForever {
            assertEquals(expectedPeriod, it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun returnsNull_ifNoDateDataHasBeenInserted() {
        assertMostRecentPeriodIs("2022-01-01", null)
    }

    @Test
    @Throws(Exception::class)
    fun returnsNull_ifNoDateDataHasPeriodEvents() {
        insertDates(noEventDate("2022-01-01"))
        assertMostRecentPeriodIs("2022-01-01", null)
    }

    @Test
    @Throws(Exception::class)
    fun returnsNull_ifAllExistingDateDataIsAfterGivenDate() {
        insertDates(periodStartDate("2022-02-02"))
        assertMostRecentPeriodIs("2022-01-01", null)
    }

    @Test
    @Throws(Exception::class)
    fun returnsPeriodThatStartsOnTheDate_IfOneExists() {
        insertDates(
            periodStartDate("2022-01-01"),
            periodStartDate("2021-12-16"),
            periodEndDate("2021-12-21"),
        )
        assertMostRecentPeriodIs("2022-01-01", Period(startDate = LocalDate.parse("2022-01-01")))
    }

    @Test
    @Throws(Exception::class)
    fun returnsMostRecentOnGoingPeriodThatStartedBeforeTheDate_IfNoPeriodStartedOnTheDate() {
        insertDates(
            periodStartDate("2021-12-16"),
            periodEndDate("2021-12-21"),
            periodStartDate("2022-01-29"),
        )
        assertMostRecentPeriodIs("2022-02-04", Period(startDate = LocalDate.parse("2022-01-29")))
    }

    @Test
    @Throws(Exception::class)
    fun returnsMostRecentCompletedPeriodThatStartedBeforeTheDate_IfNoPeriodIsOnGoingOnTheDate() {
        insertDates(
            periodStartDate("2021-12-16"),
            periodEndDate("2021-12-21"),
            periodStartDate("2022-01-14"),
            periodEndDate("2022-01-19"),
        )
        assertMostRecentPeriodIs(
            "2022-02-04",
            Period(
                startDate = LocalDate.parse("2022-01-14"),
                endDate = LocalDate.parse("2022-01-19"),
                periodLength = 6
            )
        )
    }
}