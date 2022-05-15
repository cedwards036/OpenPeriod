package dev.christopheredwards.openperiod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.christopheredwards.openperiod.data.PeriodDatabase
import dev.christopheredwards.openperiod.data.PeriodRepository
import dev.christopheredwards.openperiod.databinding.FragmentHomeBinding
import dev.christopheredwards.openperiod.viewmodels.HomeViewModel
import dev.christopheredwards.openperiod.viewmodels.HomeViewModelFactory
import java.time.LocalDate

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = PeriodRepository(PeriodDatabase.getInstance(application).pDateDao)
        val viewModelFactory = HomeViewModelFactory(dataSource, LocalDate.now(), application)
        val homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}