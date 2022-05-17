package dev.christopheredwards.openperiod

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.christopheredwards.openperiod.data.PDate
import dev.christopheredwards.openperiod.data.PeriodDatabase
import dev.christopheredwards.openperiod.data.PeriodRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.IOException
import java.time.LocalDate

abstract class PeriodRepositoryTest {
    protected lateinit var repository: PeriodRepository
    private lateinit var db: PeriodDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PeriodDatabase::class.java
        ).build()
        repository = PeriodRepository(db.periodDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // helper methods
    protected fun periodStartDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = true,
            periodEnded = false
        )
    }

    protected fun periodEndDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = false,
            periodEnded = true
        )
    }


    protected fun periodStartAndEndDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = true,
            periodEnded = true
        )
    }


    protected fun noEventDate(date: String): PDate {
        return PDate(
            date = LocalDate.parse(date),
            periodStarted = false,
            periodEnded = false
        )
    }

    protected fun insertDates(vararg dates: PDate) {
        runBlocking {
            for (date in dates) {
                repository.insertPDate(date)
            }
        }
    }
}