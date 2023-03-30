package com.example.mesibajk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mesibajk.model.Bike
import com.example.mesibajk.database.DatabaseHelper
import com.example.mesibajk.R
import com.example.mesibajk.databinding.ActivityBikeStatsBinding
import com.example.mesibajk.model.Ride
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.RuntimeExceptionDao

class BikeStatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBikeStatsBinding
    lateinit var dbHelper: DatabaseHelper
    lateinit var bikeDao: RuntimeExceptionDao<Bike, Int>
    lateinit var rideDao: RuntimeExceptionDao<Ride, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBikeStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instantiate helper and dao
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        bikeDao = dbHelper.getBikeRuntimeExceptionDao()!!
        rideDao = dbHelper.getRideRuntimeExceptionDao()!!

        // current bike info
        val currentBikeName = intent.getStringExtra("bike")
        val bikeList = bikeDao?.queryForEq("name", currentBikeName)
        var currentBikeId = 0
        if (bikeList != null) {
            currentBikeId = bikeList[0].id
        }

        val currentBikeRides = rideDao.queryForEq("id_bike", currentBikeId)
        var totalDistance = 0
        for (r in currentBikeRides) {
            totalDistance += r.distance!!
        }
        binding.bikeTitleText.text = " -  $currentBikeName"
        binding.sumDistance.text = totalDistance.toString()
        binding.sumRides.text = currentBikeRides.size.toString()

        // add more info about current bike
    }
}