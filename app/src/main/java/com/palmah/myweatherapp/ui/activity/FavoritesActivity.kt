package com.palmah.myweatherapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.ui.fragment.FavoritesFragment

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fav_fragment_container, FavoritesFragment.newInstance())
                .commitNow()
        }
    }
}