package com.palmah.myweatherapp.ui.fragment

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.databinding.FragmentWeatherInfoBinding
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.ui.adapter.WeatherDetailsListAdapter
import com.palmah.myweatherapp.utility.AndroidUtility
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.Constants.WEATHER_DATA
import com.palmah.myweatherapp.utility.WeatherUtility
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

        arguments?.let {
            weather = it.getParcelable(WEATHER_DATA) as Weather?
        }

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

        binding.favoriteButton.setOnClickListener {
            handleAddToFavorites()
        }

        binding.cityAutoCompleteTextView.doAfterTextChanged {
            it?.let {
                if(it.toString().length > 0){
                    getCurrentWeatherByCity(binding.cityAutoCompleteTextView.text.toString())
                }
            }
        }
        weather?.apply {
            binding.cityAutoCompleteTextView.visibility = View.GONE
            weather = WeatherUtility.formatWeatherObject(this,requireActivity().application)
            viewModel.weatherMutableLiveData.postValue(weather)
            viewModel.isDataAvailableMutableLiveData.postValue(true)
            viewModel.isErrorOccuredMutableLiveData.postValue(false)
        }
        observerWeatherData()
        getAllCities()
    }

    /**
     *  @param cityName - name of the city for which we need to fetch the weather data
     *  This method fetches the weather data from server if network is available otherwise fetch data from firestore if available.
     */
    private fun getCurrentWeatherByCity(cityName: String){
        viewModel.getCurrentWeatherByCity(cityName,AndroidUtility.isNetworkAvailable(requireContext()))
    }

    /**
     * This method observes the weather data in viewmodel and updates the weather info in UI.
     */
    private fun observerWeatherData(){
        viewModel.weatherMutableLiveData.observe(this,  Observer {
            Log.d(TAG,"Weather mutable livedata observer called: ${it.toString()}")
            it?.let {
                buildWeatherDetailsListData(it)
                weatherDetailsListAdapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * This method fetches all the cached citi names from firestore.
     * The city list fetched is used as input to cityAutoCompleteTextView.
     */
    private fun getAllCities(){
        viewModel.getAllCities()
        viewModel.cityListMutableLiveData.observe(this, Observer { cityList->
            cityArrayList.clear()
            cityArrayList.addAll(cityList)
            cityListAdapter?.notifyDataSetChanged()
        })
    }

    /**
     * @param weather - weather object of the searched city.
     * This method prepares the weather data to displayed in UI.
     * This methods constructs data for weatherDetailsListAdapter.
     */
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

    /**
     * This method makes a copy of the weather object in viewmodel and invert the favorite property value and save to firestore.
     */
    private fun handleAddToFavorites(){
        viewModel.weatherMutableLiveData.value?.let {
            var isFavorite = it.favorite
            it.copy(it.cityName,it.id,it.temp,it.minTemp,it.maxTemp,it.pressure,it.humidity,it.weatherDescription,
                it.windSpeed,it.windDegree,it.visibility,it.country,it.timeStamp,!isFavorite).let { weatherCopy->
                    viewModel.saveToFavorites(weatherCopy)
                    viewModel.formatWeatherObject(weatherCopy).let {
                        viewModel.weatherMutableLiveData.postValue(it)
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }}