package com.palmah.myweatherapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.palmah.myweatherapp.databinding.FragmentWeatherInfoBinding
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.viewmodel.WeatherInfoViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherInfoFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherInfoFragment()
    }

    private var _binding: FragmentWeatherInfoBinding? = null

    private lateinit var viewModel: WeatherInfoViewModel

    private var weather : Weather? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeatherInfoBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherInfoViewModel::class.java)

        binding.lifecycleOwner = this
        binding.weatherInfoViewModel = viewModel

        binding.buttonFirst.setOnClickListener {
            getCurrentWeatherByCity("Mumbai")
        }

        getCurrentWeatherByCity("Madurai")
    }

    private fun getCurrentWeatherByCity(cityName: String){
        viewModel.getCurrentWeatherByCity(cityName)

        viewModel.weatherMutableLiveData.observe(this,  Observer {
            Log.d(TAG,"Weather mutable livedata observer called: ${it.toString()}")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }}