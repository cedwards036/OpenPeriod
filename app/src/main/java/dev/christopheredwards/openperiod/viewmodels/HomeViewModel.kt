package dev.christopheredwards.openperiod.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dev.christopheredwards.openperiod.R
import dev.christopheredwards.openperiod.data.PDate
import dev.christopheredwards.openperiod.data.PeriodRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    private val dataSource: PeriodRepository,
    private val currentDate: LocalDate,
    application: Application
) : AndroidViewModel(application) {

    private var today = MutableLiveData<PDate>()
    private var isOnPeriod = MutableLiveData<Boolean>()

    val dayCountLabel = Transformations.map(isOnPeriod) { isOnPeriod ->
        if (isOnPeriod) application.resources.getString(
            R.string.days_remaining_in_period_label
        )
        else application.resources.getString(
            R.string.days_until_next_period_label
        )
    }

    init {
        initializeToday()
        setIsOnPeriod()
    }

    private fun initializeToday() {
        viewModelScope.launch {
            today.value = getToday()
        }
    }

    private fun setIsOnPeriod() {
        viewModelScope.launch {
            isOnPeriod.value = isOnPeriod()
        }
    }

    private suspend fun getToday(): PDate {
        var pDate = dataSource.getByDate(currentDate)
        if (pDate == null) {
            pDate = PDate(date = currentDate)
        }
        return pDate
    }

    fun handlePeriodStartedClick() {
        Log.i("HomeViewModel", dayCountLabel.value.toString())
        viewModelScope.launch {
            startPeriod()
            setIsOnPeriod()
        }
    }

    private suspend fun startPeriod() {
        today.value?.let {
            it.periodStarted = true
            if (dataSource.getByDate(currentDate) == null) {
                dataSource.insert(it)
            } else {
                dataSource.update(it)
            }
        }
    }

    private suspend fun isOnPeriod(): Boolean {
        return dataSource.isOnPeriod(currentDate)
    }


    val currentDateString = Transformations.map(today) {
        it.date.toString()
    }

    val daysUntilNextPeriodString = "11"
    // TODO: get the "period started"/"period ended" home screen button to work as expected
}