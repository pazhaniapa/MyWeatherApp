package com.palmah.myweatherapp.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.databinding.FragmentWeatherInfoBinding
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.ui.adapter.WeatherDetailsListAdapter
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
    private var cityArrayList = ArrayList<String>()
    private var cityListAdapter: ArrayAdapter<String>? = null
    private var weatherDetailsMapList = ArrayList<Map<String,String>>()
    private lateinit var weatherDetailsListAdapter : WeatherDetailsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeatherInfoBinding.inflate(inflater,container,false)

        cityListAdapter =
            ArrayAdapter<String>(
                requireActivity().applicationContext,
                android.R.layout.select_dialog_item,
                cityArrayList
            )

        binding.cityAutoCompleteTextView.threshold = 1
        binding.cityAutoCompleteTextView.setAdapter(cityListAdapter)
        binding.cityAutoCompleteTextView.setTextColor(Color.BLACK)

        weatherDetailsListAdapter = WeatherDetailsListAdapter(requireContext(),weatherDetailsMapList)
        binding.weatherDetailsListView.adapter = weatherDetailsListAdapter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherInfoViewModel::class.java)

        binding.lifecycleOwner = this
        binding.weatherInfoViewModel = viewModel

       /* binding.buttonFirst.setOnClickListener {
            getCurrentWeatherByCity("Madurai")
        }*/

        binding.searchButton.setOnClickListener {
            getCurrentWeatherByCity(binding.cityAutoCompleteTextView.text.toString())
        }

        //getCurrentWeatherByCity("Trichy")

        getFavoriteCitiesWeatherInfoList()

        getAllCities()


    }

    private fun getCurrentWeatherByCity(cityName: String){
        viewModel.getCurrentWeatherByCity(cityName)

        viewModel.weatherMutableLiveData.observe(this,  Observer {
            Log.d(TAG,"Weather mutable livedata observer called: ${it.toString()}")
            it?.let {
                buildWeatherDetailsListData(it)
                weatherDetailsListAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun getFavoriteCitiesWeatherInfoList(){
        viewModel.getFavoriteCitiesWeatherInfoList()

    }

    private fun getAllCities(){
        viewModel.getAllCities()
        viewModel.cityListMutableLiveData.observe(this, Observer { cityList->
            cityArrayList.clear()
            cityArrayList.addAll(cityList)
            cityListAdapter?.notifyDataSetChanged()

        })
    }

    private fun buildWeatherDetailsListData(weather : Weather){
        var weatherDetailsList = ArrayList<Map<String,String>>()

        HashMap<String,String>().also {
            it.put(requireActivity().resources.getString(
                R.string.label_pressure),weather.formattedPressure)
            weatherDetailsList.add(it)
        }

        HashMap<String,String>().also {
            it.put(requireActivity().resources.getString(
                R.string.label_humidity),weather.formattedHumidity)
            weatherDetailsList.add(it)
        }
        HashMap<String,String>().also {
            it.put(requireActivity().resources.getString(
                R.string.label_visibility),weather.formattedVisibility)
            weatherDetailsList.add(it)
        }
        HashMap<String,String>().also {
            it.put(requireActivity().resources.getString(
                R.string.label_wind_speed),weather.formattedWindSpeed)
            weatherDetailsList.add(it)
        }
        weatherDetailsMapList.clear()
        weatherDetailsMapList.addAll(weatherDetailsList)
    }

    private fun buildDummyWeatherDetailsListData(){
        var weatherDetailsList = ArrayList<Map<String,String>>()

        HashMap<String,String>().also {
            it.put(requireActivity().resources.getString(
                R.string.label_pressure),"101")
            weatherDetailsList.add(it)
        }

        HashMap<String,String>().also {
            it.put(requireActivity().resources.getString(
                R.string.label_humidity),"201")
            weatherDetailsList.add(it)
        }

        weatherDetailsMapList = weatherDetailsList


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }}