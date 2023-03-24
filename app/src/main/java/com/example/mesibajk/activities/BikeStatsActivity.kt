package com.example.mesibajk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mesibajk.model.Bike
import com.example.mesibajk.database.DatabaseHelper
import com.example.mesibajk.R
import com.example.mesibajk.model.Ride
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.RuntimeExceptionDao

class BikeStatsActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var bikeDao: RuntimeExceptionDao<Bike, Int>
    lateinit var rideDao: RuntimeExceptionDao<Ride, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike_stats)

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

        val bikeNameView = findViewById<TextView>(R.id.bikeTitleText)
        val sumDistanceView = findViewById<TextView>(R.id.sumDistance)
        val sumRidesView = findViewById<TextView>(R.id.sumRides)

        val currentBikeRides = rideDao.queryForEq("id_bike", currentBikeId)
        var totalDistance = 0
        for (r in currentBikeRides) {
            totalDistance += r.distance!!
        }
        bikeNameView.text = " -  " + currentBikeName
        sumDistanceView.text = totalDistance.toString()
        sumRidesView.text = currentBikeRides.size.toString()

    }
}