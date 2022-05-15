package dev.christopheredwards.openperiod

import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.christopheredwards.openperiod.data.PDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class BasicDBOperationsTest : PeriodRepositoryTest() {

    @Test
    @Throws(Exception::class)
    fun canRetrievePDate_afterItHasBeenPersisted(): Unit = runBlocking {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate = PDate(id = 1, date = localDate, wasIntimate = true)
        repository.insert(pDate)
        val retrievedPDate = repository.getByDate(localDate)
        assertEquals(pDate, retrievedPDate)
    }

    @Test
    @Throws(Exception::class)
    fun pDateHasNewValues_afterItHasBeenUpdated(): Unit = runBlocking {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate = PDate(id = 1, date = localDate, wasIntimate = true)
        repository.insert(pDate)
        pDate.periodStarted = true
        repository.update(pDate)
        repository.getByDate(localDate)?.periodStarted?.let { assertTrue(it) }
    }

    @Test
    @Throws(Exception::class)
    fun pDateCantBeRetrieved_afterItHasBeenDeleted(): Unit = runBlocking {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate = PDate(id = 1, date = localDate, wasIntimate = true)
        repository.insert(pDate)
        repository.delete(pDate)
        assertNull(repository.getByDate(localDate))
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insertThrowsError_ifRecordWithTheSameDateExists(): Unit = runBlocking {
        val localDate = LocalDate.parse("2022-01-01")
        val pDate1 = PDate(id = 1, date = localDate)
        val pDate2 = PDate(id = 2, date = localDate)
        repository.insert(pDate1)
        repository.insert(pDate2)
    }
}