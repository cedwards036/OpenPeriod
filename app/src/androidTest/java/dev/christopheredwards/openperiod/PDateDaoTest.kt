package dev.christopheredwards.openperiod

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.db.PDate
import dev.christopheredwards.openperiod.db.PDateDao
import dev.christopheredwards.openperiod.db.PeriodDatabase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class PDateDaoTest {
    private lateinit var pDateDao: PDateDao
    private lateinit var db: PeriodDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PeriodDatabase::class.java
        ).build()
        pDateDao = db.pDateDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun canRetrievePDate_afterItHasBeenPersisted() {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate = PDate(id = 1, date = localDate, wasIntimate = true)
        pDateDao.insert(pDate)
        val retrievedPDate = pDateDao.getByDate(localDate)
        assertEquals(pDate, retrievedPDate)
    }

    @Test
    @Throws(Exception::class)
    fun pDateHasNewValues_afterItHasBeenUpdated() {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate = PDate(id = 1, date = localDate, wasIntimate = true)
        pDateDao.insert(pDate)
        pDate.periodStarted = true
        pDateDao.update(pDate)
        pDateDao.getByDate(localDate)?.periodStarted?.let { assertTrue(it) }
    }

    @Test
    @Throws(Exception::class)
    fun pDateCantBeRetrieved_afterItHasBeenDeleted() {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate = PDate(id = 1, date = localDate, wasIntimate = true)
        pDateDao.insert(pDate)
        pDateDao.delete(pDate)
        assertNull(pDateDao.getByDate(localDate))
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertThrowsError_ifRecordWithTheSameDateExists() {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate1 = PDate(id = 1, date = localDate, wasIntimate = true)
        val pDate2 = PDate(id = 2, date = localDate, wasIntimate = true)
        pDateDao.insert(pDate1)
        pDateDao.insert(pDate2)
    }
}