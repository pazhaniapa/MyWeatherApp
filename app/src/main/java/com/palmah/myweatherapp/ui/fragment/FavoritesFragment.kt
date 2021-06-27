package com.palmah.myweatherapp.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.ui.adapter.FavoriteCityWeatherListAdapter
import com.palmah.myweatherapp.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var weatherListRecyclerView : RecyclerView
    private lateinit var weatherListRecyclerAdapter : FavoriteCityWeatherListAdapter
    private var weatherList = ArrayList<Weather>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        //initialising and setting up weather list recycler view
        weatherListRecyclerView = view.findViewById(R.id.fav_recycler_view)
        weatherListRecyclerAdapter = FavoriteCityWeatherListAdapter(requireActivity().application,requireActivity(),weatherList)
        val layoutManager = LinearLayoutManager(requireActivity())
        weatherListRecyclerView.layoutManager = layoutManager
        weatherListRecyclerView.itemAnimator = DefaultItemAnimator()
        weatherListRecyclerView.adapter = weatherListRecyclerAdapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        getFavoriteCitiesWeatherInfoList()
    }

    /**
     * This method fetches the list of weather data by city name which has been marked as favorite by user.
     * Fetches from firestore cache.
     */
    private fun getFavoriteCitiesWeatherInfoList(){
        viewModel.getFavoriteCitiesWeatherInfoList()

        viewModel.weatherMutableList.observe(this,{
            it?.let {
                weatherList.clear()
                weatherList.addAll(it)
                weatherListRecyclerAdapter.notifyDataSetChanged()
            }
        })
    }

}