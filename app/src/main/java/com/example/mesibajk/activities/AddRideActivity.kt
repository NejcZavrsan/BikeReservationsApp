package com.example.mesibajk.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mesibajk.*
import com.example.mesibajk.database.DatabaseHelper
import com.example.mesibajk.databinding.ActivityAddRideBinding
import com.example.mesibajk.fragments.DatePickerFragment
import com.example.mesibajk.fragments.TimePickerFragment
import com.example.mesibajk.model.Bike
import com.example.mesibajk.model.Ride
import com.google.android.material.textfield.TextInputLayout
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.RuntimeExceptionDao
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddRideActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddRideBinding
    lateinit var dbHelper: DatabaseHelper
    var rideTime: MutableMap<String, String> = mutableMapOf()
    var seekBarEndpoint: Int = 3
    lateinit var inputDepartment: String
    lateinit var inputPurpose: String
    var inputBike: String = ""
    lateinit var bikeDao: RuntimeExceptionDao<Bike, Int>
    lateinit var rideDao: RuntimeExceptionDao<Ride, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup database access
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        bikeDao = dbHelper.getBikeRuntimeExceptionDao()!!
        rideDao = dbHelper.getRideRuntimeExceptionDao()!!

        // setup UI
        setupDatePickers()
        setupSpinners()
        setupDistanceSeekBar()

        // below code for saving the bike reservation
        binding.addRideBtn.setOnClickListener {
            if (binding.editTextRider.text.toString().isNotBlank()) {
                addBikeReservation()
            } else {
                // Show errors on fields that are not valid
                binding.editTextRiderTil.error = if (binding.editTextRider.text.isBlank()) {
                    getString(R.string.required_field)
                } else null
            }
        }
    }

    private fun addBikeReservation() {
        // get bike id
        val inputBikeList = bikeDao?.queryForEq("name", inputBike)
        var inputBikeId = 0
        if (inputBikeList != null) {
            inputBikeId = inputBikeList[0].id
        }

        // get reservation times ("yyyy-MM-dd HH:mm:ss" -> unix)
        val startDateTime =  rideTime["start_YYYY"] +
                "-" +
                rideTime["start_MM"] +
                "-" +
                rideTime["start_DD"] +
                " " +
                rideTime["start_hh"] +
                ":" +
                rideTime["start_mm"] +
                ":00"
        val endDateTime =  rideTime["end_YYYY"] +
                "-" +
                rideTime["end_MM"] +
                "-" +
                rideTime["end_DD"] +
                " " +
                rideTime["end_hh"] +
                ":" +
                rideTime["end_mm"] +
                ":00"
        val startUnixTime = dateToUnix(startDateTime)
        val endUnixTime = dateToUnix(endDateTime)
        val currentUnixTime = System.currentTimeMillis()
        Log.d("time", "CurrentUnixTime in minutes: ${currentUnixTime/60000}")
        Log.d("time", "StartUnixTime in minutes: ${startUnixTime!!/60000}")
        Log.d("time", "EndUnixTime in minutes: ${endUnixTime!!/60000}")

        if (startUnixTime != null && endUnixTime != null) {

            val newRide = Ride(
                inputBikeId,
                binding.editTextRider.text.toString(),
                inputDepartment,
                startUnixTime!!,
                endUnixTime!!,
                seekBarEndpoint,
                inputPurpose
            )
            rideDao!!.create(newRide)
            val listOfRides = rideDao!!.queryForAll()
            Log.d("test", listOfRides.toString())
            Toast.makeText(this, "Rezervacija uspešno shranjena", Toast.LENGTH_SHORT).show()

            // Tell main activity which bike in recyclerView to refresh
            val intentBikeStats = Intent(this, MainActivity::class.java)
            intentBikeStats.putExtra("reserved_bike", inputBikeId)
            startActivity(intentBikeStats)
        } else {
            Toast.makeText(this, "Izberite termin rezervacije", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupDatePickers() {
        binding.startTime.setOnClickListener {
            TimePickerFragment(rideTime, binding.startTime, binding.endTime).show(supportFragmentManager, "startTimePicker")
        }

        binding.startDate.setOnClickListener {
            DatePickerFragment(rideTime, binding.startDate, binding.endDate).show(supportFragmentManager, "startDatePicker")
        }

        binding.endTime.setOnClickListener {
            TimePickerFragment(rideTime, binding.startTime, binding.endTime).show(supportFragmentManager, "endTimePicker")
        }

        binding.endDate.setOnClickListener {
            DatePickerFragment(rideTime, binding.startDate, binding.endDate).show(supportFragmentManager, "endDatePicker")
        }
    }

    private fun setupDistanceSeekBar() {
        binding.seekBarDistance.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                binding.textDistance.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                if (seekbar != null) {
                    seekBarEndpoint = seekbar.progress
                }
            }
        })
    }

    private fun setupSpinners() {

        // Setup spinner for bike selection
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            getBikeNames()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBike.adapter = adapter
        }
        binding.spinnerBike.onItemSelectedListener = this

        // Setup spinner for department selection
        ArrayAdapter.createFromResource(
            this,
            R.array.departments_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDepartment.adapter = adapter
        }
        binding.spinnerDepartment.onItemSelectedListener = this

        // Setup spinner for purpose selection
        ArrayAdapter.createFromResource(
            this,
            R.array.purpose_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPurpose.adapter = adapter
        }
        binding.spinnerPurpose.onItemSelectedListener = this
    }

    fun dateToUnix(dateToConvert: String): Long? {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN)
            val date: Date? = format.parse(dateToConvert)
            val timestamp: Long? = date?.time
            return timestamp
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val selectedItem = parent?.getItemAtPosition(position)
        when (parent) {
            binding.spinnerBike -> inputBike = selectedItem as String
            binding.spinnerDepartment -> inputDepartment = selectedItem as String
            binding.spinnerPurpose -> inputPurpose = selectedItem as String
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun getBikeNames(): ArrayList<String> {
        val bikeList = bikeDao.queryForAll()
        val bikeNames = arrayListOf<String>()
        for (bike in bikeList) {
            bikeNames.add(bike.name)
        }
        return bikeNames
    }
}