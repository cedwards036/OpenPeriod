package dev.christopheredwards.openperiod

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.db.Period
import dev.christopheredwards.openperiod.db.PeriodDao
import dev.christopheredwards.openperiod.db.PeriodDatabase
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class PeriodDaoTest{
    private lateinit var periodDao: PeriodDao
    private lateinit var db: PeriodDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PeriodDatabase::class.java
        ).build()
        periodDao = db.periodDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_WhenNoPeriodsHaveBeenAdded() {
        assertFalse(periodDao.isOnPeriod())
    }

    @Test
    @Throws(Exception::class)
    fun userIsNotOnPeriod_WhenNoPeriodHasANullEndDate() {
        periodDao.insert(
            Period(
                startDate = LocalDate.parse("2022-01-01"),
                endDate = LocalDate.parse("2022-01-05")
            )
        )
        assertFalse(periodDao.isOnPeriod())
    }

    @Test
    @Throws(Exception::class)
    fun userIsOnPeriod_WhenPeriodHasBeenAddedWithNoEndDate() {
        periodDao.insert(Period(startDate = LocalDate.parse("2022-01-01")))
        assertTrue(periodDao.isOnPeriod())
    }
}