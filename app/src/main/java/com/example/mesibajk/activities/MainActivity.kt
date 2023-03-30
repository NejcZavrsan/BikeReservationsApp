package com.example.mesibajk.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mesibajk.BikeAdapter
import com.example.mesibajk.R
import com.example.mesibajk.database.DatabaseHelper
import com.example.mesibajk.model.Bike
import com.example.mesibajk.model.Ride
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.RuntimeExceptionDao

class MainActivity : AppCompatActivity(), BikeAdapter.OnBikeClickListener {

    lateinit var dbHelper: DatabaseHelper
    lateinit var sharedPreferences: SharedPreferences
    lateinit var bikeDao: RuntimeExceptionDao<Bike, Int>
    lateinit var rideDao: RuntimeExceptionDao<Ride, Int>
    private lateinit var bikesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate helper and dao
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        bikeDao = dbHelper.getBikeRuntimeExceptionDao()!!
        rideDao = dbHelper.getRideRuntimeExceptionDao()!!

        // Check first launch info and create the bikes
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val isFirstLaunch: Boolean = sharedPreferences.getBoolean(FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            createSevenBikes()
        }

        bikesRecyclerView = findViewById<RecyclerView>(R.id.recycler_view_bikes).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = BikeAdapter(getBikes(), getRides(), this@MainActivity, this@MainActivity)
        }

        // Add the add-ride button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intentAddRide = Intent(this, AddRideActivity::class.java)
            startActivity(intentAddRide)
        }
    }

    override fun onResume() {
        super.onResume()

        // Refresh reserved bike, if we're coming from AddRideActivity
        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey("reserved_bike")) {
                val reservedBike = extras.getInt("reserved_bike")
                bikesRecyclerView.adapter?.notifyItemChanged(reservedBike)
            }
        }

        setFirstLaunchFalse()
    }

    private fun createSevenBikes() {
        for (i in 1 .. 8) {
            bikeDao.createOrUpdate(Bike(i, "Bajki$i"))
        }
    }

    private fun getBikes(): List<Bike> {
        return bikeDao.queryForAll()
    }

    private fun getRides(): List<Ride> {
        return rideDao.queryForAll()
    }

    private fun setFirstLaunchFalse() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(FIRST_LAUNCH, false)
        editor.commit()
    }

    override fun onBikeViewClick(position: Int) {
        val clickedBike = bikeDao.queryForId(position + 1)
        val intentBikeStats = Intent(this, BikeStatsActivity::class.java)
        intentBikeStats.putExtra("bike", clickedBike.name)
        startActivity(intentBikeStats)
    }

    companion object {
        const val MyPREFERENCES = "shared_preferences"
        const val FIRST_LAUNCH = "first_launch"
    }
}