package dev.christopheredwards.openperiod

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.christopheredwards.openperiod.data.PeriodDatabase
import dev.christopheredwards.openperiod.data.PeriodRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.IOException

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
}