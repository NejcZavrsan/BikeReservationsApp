package com.example.mesibajk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mesibajk.R
import com.j256.ormlite.android.apptools.OpenHelperManager

class AddRideActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var dbHelper: DatabaseHelper

    lateinit var spinnerBike: Spinner
    lateinit var spinnerDepartment: Spinner
    lateinit var spinnerPurpose: Spinner
    lateinit var inputDepartment: String
    lateinit var inputPurpose: String
    var inputBike: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ride)

        setupSpinners()

        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        val bikeDao = dbHelper.getBikeRuntimeExceptionDao()
        val rideDao = dbHelper.getRideRuntimeExceptionDao()



        val inputRiderView = findViewById<EditText>(R.id.edit_text_rider)
        val inputStartTime = findViewById<EditText>(R.id.startTime)
        val inputEndTime = findViewById<EditText>(R.id.endTime)

        val inputDistance = findViewById<SeekBar>(R.id.seekBarKm)

        // below code for saving the bike reservation
        val addRideButton = findViewById<Button>(R.id.add_ride_btn)
        addRideButton.setOnClickListener {
            Log.d("test", "All bikes: ${bikeDao?.queryForAll()}")
            Log.d("test", "Input bike: $inputBike")
            val inputBikeList = bikeDao?.queryForEq("name", inputBike)
            var inputBikeId = 0
            if (inputBikeList != null) {
                inputBikeId = inputBikeList[0].id
            }

            val newRide = Ride(
                inputBikeId,
                inputRiderView.text.toString(),
                inputDepartment,
                inputStartTime.text.toString().toInt(),
                inputEndTime.text.toString().toInt(),
                5,
                inputPurpose
            )
            rideDao!!.create(newRide)
            val listOfRides = rideDao!!.queryForAll()
            Log.d("test", listOfRides.toString())
        }
    }

    private fun setupSpinners() {

        // Setup spinner for bike selection
        spinnerBike = findViewById<Spinner>(R.id.spinner_bike)
        ArrayAdapter.createFromResource(
            this,
            R.array.bikes_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerBike.adapter = adapter
        }
        spinnerBike.onItemSelectedListener = this

        // Setup spinner for department selection
        spinnerDepartment = findViewById<Spinner>(R.id.spinner_department)
        ArrayAdapter.createFromResource(
            this,
            R.array.departments_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerDepartment.adapter = adapter
        }
        spinnerDepartment.onItemSelectedListener = this

        // Setup spinner for purpose selection
        spinnerPurpose = findViewById<Spinner>(R.id.spinner_purpose)
        ArrayAdapter.createFromResource(
            this,
            R.array.purpose_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerPurpose.adapter = adapter
        }
        spinnerPurpose.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val selectedItem = parent?.getItemAtPosition(position)
        when (parent) {
            spinnerBike -> inputBike = selectedItem as String
            spinnerDepartment -> inputDepartment = selectedItem as String
            spinnerPurpose -> inputPurpose = selectedItem as String
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d("test", "YOU HAVE TO SELECT STH IN SPINNERS")
    }

}