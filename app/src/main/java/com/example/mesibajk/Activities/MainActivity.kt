package com.example.mesibajk.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.mesibajk.Bike
import com.example.mesibajk.DatabaseHelper
import com.example.mesibajk.R
import com.example.mesibajk.Ride
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.RuntimeExceptionDao

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var sharedPreferences: SharedPreferences
    lateinit var bikeDao: RuntimeExceptionDao<Bike, Int>
    lateinit var rideDao: RuntimeExceptionDao<Ride, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate helper and dao
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        bikeDao = dbHelper.getBikeRuntimeExceptionDao()!!
        rideDao = dbHelper.getRideRuntimeExceptionDao()!!

        // Check first launch info
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val isFirstLaunch: Boolean = sharedPreferences.getBoolean(FIRST_LAUNCH, true)
        if (isFirstLaunch) {
            createSevenBikes()
        }


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intentAddRide = Intent(this, AddRideActivity::class.java)
            startActivity(intentAddRide)
        }
    }

    override fun onResume() {
        super.onResume()
        populateUI()
        setFirstLaunchFalse()
    }

    private fun createSevenBikes() {

        bikeDao.createOrUpdate(Bike(1, "Bike1"))
        bikeDao.createOrUpdate(Bike(2, "Bike2"))
        bikeDao.createOrUpdate(Bike(3, "Bike3"))
        bikeDao.createOrUpdate(Bike(4, "Bike4"))
        bikeDao.createOrUpdate(Bike(5, "Bike5"))
        bikeDao.createOrUpdate(Bike(6, "Bike6"))
        bikeDao.createOrUpdate(Bike(7, "Bike7"))
    }

    private fun populateUI() {

        val listOfBikes = bikeDao.queryForAll()
        val bike1 = bikeDao.queryForId(1)
        val bike2 = bikeDao.queryForId(2)
        val bike3 = bikeDao.queryForId(3)
        val bike4 = bikeDao.queryForId(4)
        val bike5 = bikeDao.queryForId(5)
        val bike6 = bikeDao.queryForId(6)
        val bike7 = bikeDao.queryForId(7)

        Log.d("test", listOfBikes.toString())

        val bike1Name = findViewById<TextView>(R.id.bike1_name)
        val bike2Name = findViewById<TextView>(R.id.bike2_name)
        val bike3Name = findViewById<TextView>(R.id.bike3_name)
        val bike4Name = findViewById<TextView>(R.id.bike4_name)
        val bike5Name = findViewById<TextView>(R.id.bike5_name)
        val bike6Name = findViewById<TextView>(R.id.bike6_name)
        val bike7Name = findViewById<TextView>(R.id.bike7_name)
        val bike1Occupancy = findViewById<TextView>(R.id.bike1_occupancy)
        val bike2Occupancy = findViewById<TextView>(R.id.bike2_occupancy)
        val bike3Occupancy = findViewById<TextView>(R.id.bike3_occupancy)
        val bike4Occupancy = findViewById<TextView>(R.id.bike4_occupancy)
        val bike5Occupancy = findViewById<TextView>(R.id.bike5_occupancy)
        val bike6Occupancy = findViewById<TextView>(R.id.bike6_occupancy)
        val bike7Occupancy = findViewById<TextView>(R.id.bike7_occupancy)
        val bike1Icon = findViewById<ImageView>(R.id.bike1_icon)
        val bike2Icon = findViewById<ImageView>(R.id.bike2_icon)
        val bike3Icon = findViewById<ImageView>(R.id.bike3_icon)
        val bike4Icon = findViewById<ImageView>(R.id.bike4_icon)
        val bike5Icon = findViewById<ImageView>(R.id.bike5_icon)
        val bike6Icon = findViewById<ImageView>(R.id.bike6_icon)
        val bike7Icon = findViewById<ImageView>(R.id.bike7_icon)

        bike1Name.text = bike1.name
        bike2Name.text = bike2.name
        bike3Name.text = bike3.name
        bike4Name.text = bike4.name
        bike5Name.text = bike5.name
        bike6Name.text = bike6.name
        bike7Name.text = bike7.name

        // determine bike occupancies
        val currentListOfRides = rideDao.queryForAll()
        val currentUnixTime = System.currentTimeMillis()
        Log.d("test", "CurrentUnixTime: $currentUnixTime")
        var bike1Occupied = false
        var bike2Occupied = false
        var bike3Occupied = false
        var bike4Occupied = false
        var bike5Occupied = false
        var bike6Occupied = false
        var bike7Occupied = false
        for (ride in currentListOfRides) {
            if (ride.start_time!! <= currentUnixTime && ride.end_time!! >= currentUnixTime) {
                when (ride.id_bike) {
                    1 -> {
                        bike1Occupied = true
                    }
                    2 -> {
                        bike2Occupied = true
                    }
                    3 -> {
                        bike3Occupied = true
                    }
                    4 -> {
                        bike4Occupied = true
                    }
                    5 -> {
                        bike5Occupied = true
                    }
                    6 -> {
                        bike6Occupied = true
                    }
                    7 -> {
                        bike7Occupied = true
                    }
                }
            }
        }
        if (bike1Occupied) {
            bike1Occupancy.text = getText(R.string.occupied)
            bike1Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike1Occupancy.text = getText(R.string.frej)
            bike1Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }
        if (bike2Occupied) {
            bike2Occupancy.text = getText(R.string.occupied)
            bike2Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike2Occupancy.text = getText(R.string.frej)
            bike2Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }

        if (bike3Occupied) {
            bike3Occupancy.text = getText(R.string.occupied)
            bike3Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike3Occupancy.text = getText(R.string.frej)
            bike3Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }

        if (bike4Occupied) {
            bike4Occupancy.text = getText(R.string.occupied)
            bike4Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike4Occupancy.text = getText(R.string.frej)
            bike4Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }

        if (bike5Occupied) {
            bike5Occupancy.text = getText(R.string.occupied)
            bike5Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike5Occupancy.text = getText(R.string.frej)
            bike5Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }

        if (bike6Occupied) {
            bike6Occupancy.text = getText(R.string.occupied)
            bike6Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike6Occupancy.text = getText(R.string.frej)
            bike6Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }

        if (bike7Occupied) {
            bike7Occupancy.text = getText(R.string.occupied)
            bike7Icon.setBackgroundResource(R.drawable.baseline_circle_orange)
        } else {
            bike7Occupancy.text = getText(R.string.frej)
            bike7Icon.setBackgroundResource(R.drawable.baseline_circle_green)
        }
    }

    private fun setFirstLaunchFalse() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(FIRST_LAUNCH, false)
        editor.commit()
    }

    companion object {
        const val MyPREFERENCES = "shared_preferences"
        const val FIRST_LAUNCH = "first_launch"
    }
}