package com.example.mesibajk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.j256.ormlite.android.apptools.OpenHelperManager

class AddSimpleRideActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_simple_ride)

        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        val rideDao = dbHelper.getRideRuntimeExceptionDao()

        val inputRider = findViewById<EditText>(R.id.spinner_rider)
        val inputSector = findViewById<EditText>(R.id.spinner_sector)
        val inputStartTime = findViewById<EditText>(R.id.startTime)
        val inputEndTime = findViewById<EditText>(R.id.endTime)
        val inputDistance = findViewById<EditText>(R.id.seekBarKm)
        val inputPurpose = findViewById<EditText>(R.id.spinner_purpose)
        val addRideButton = findViewById<Button>(R.id.add_ride_btn)

        addRideButton.setOnClickListener {
            val newRide = Ride(
                1,
                1,
                inputRider.text.toString(),
                inputSector.text.toString(),
                inputStartTime.text.toString().toInt(),
                inputEndTime.text.toString().toInt(),
                inputDistance.text.toString().toInt(),
                inputPurpose.text.toString()
            )
            rideDao!!.create(newRide)
            val listOfRides = rideDao!!.queryForAll()
            Log.d("data", listOfRides.toString())
        }
    }
}