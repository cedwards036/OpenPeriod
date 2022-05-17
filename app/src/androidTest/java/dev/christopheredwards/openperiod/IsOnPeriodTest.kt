package dev.christopheredwards.openperiod

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.data.PDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class IsOnPeriodTest : PeriodRepositoryTest() {

    @Before
    fun setUpData(): Unit = runBlocking {
        val dates = listOf(
            // period started and ended on same date
            PDate(
                date = LocalDate.parse("2021-12-15"),
                periodStarted = true,
                periodEnded = true
            ),
            // period started
            PDate(
                date = LocalDate.parse("2022-01-01"),
                periodStarted = true,
                periodEnded = false
            ),
            // period ended
            PDate(
                date = LocalDate.parse("2022-01-05"),
                periodStarted = false,
                periodEnded = true
            ),
            // user entered date with no period event
            PDate(
                date = LocalDate.parse("2022-01-16"),
                periodStarted = false,
                periodEnded = false
            ),
            // period started
            PDate(
                date = LocalDate.parse("2022-02-03"),
                periodStarted = true,
                periodEnded = false
            ),
            // user entered date with no period event
            PDate(
                date = LocalDate.parse("2022-02-04"),
                periodStarted = false,
                periodEnded = false
            ),
            // period ended
            PDate(
                date = LocalDate.parse("2022-02-07"),
                periodStarted = false,
                periodEnded = true
            ),
        )
        for (pDate in dates) {
            repository.insertPDate(pDate)
        }
    }

    @Test
    @Throws(Exception::class)
    fun userIsOnPeriod_ifPeriodStartedOnTheCurrentDate(): Unit =
        runBlocking {
            assertTrue(repository.isOnPeriod(LocalDate.parse("2022-01-01")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsOnPeriod_ifPeriodStartedOnMostRecentPriorDate(): Unit =
        runBlocking {
            assertTrue(repository.isOnPeriod(LocalDate.parse("2022-01-03")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsOnPeriod_ifPeriodStartedOnMostRecentPriorDateWithAnEvent(): Unit =
        runBlocking {
            assertTrue(repository.isOnPeriod(LocalDate.parse("2022-02-04")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_ifThereIsNoDataPriorToGivenDate(): Unit =
        runBlocking {
            assertFalse(repository.isOnPeriod(LocalDate.parse("2020-01-01")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_ifPeriodEndedOnTheCurrentDate(): Unit =
        runBlocking {
            assertFalse(repository.isOnPeriod(LocalDate.parse("2022-01-05")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_ifPeriodEndedOnMostRecentPriorDate(): Unit =
        runBlocking {
            assertFalse(repository.isOnPeriod(LocalDate.parse("2022-01-09")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_ifPeriodEndedOnMostRecentPriorDateWithAnEvent(): Unit =
        runBlocking {
            assertFalse(repository.isOnPeriod(LocalDate.parse("2022-01-25")))
        }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_ifPeriodStartedAndEndedOnTheCurrentOrMostRecentDate(): Unit =
        runBlocking {
            assertFalse(repository.isOnPeriod(LocalDate.parse("2021-12-15")))
            assertFalse(repository.isOnPeriod(LocalDate.parse("2021-12-17")))
        }
}