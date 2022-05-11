package dev.christopheredwards.openperiod.viewmodels

import androidx.lifecycle.ViewModel
import dev.christopheredwards.openperiod.db.PDateDao

class HomeViewModel(val database: PDateDao) : ViewModel() {
    val daysUntilNextPeriodString = "11"
}