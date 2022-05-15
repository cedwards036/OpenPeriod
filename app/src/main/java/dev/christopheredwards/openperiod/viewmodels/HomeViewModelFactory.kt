package dev.christopheredwards.openperiod.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.christopheredwards.openperiod.data.PeriodRepository
import java.time.LocalDate

class HomeViewModelFactory(
    private val dataSource: PeriodRepository,
    private val currentDate: LocalDate,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource, currentDate, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}