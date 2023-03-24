package com.example.mesibajk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.j256.ormlite.android.apptools.OpenHelperManager
import java.text.SimpleDateFormat
import java.util.*

class AddRideActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var dbHelper: DatabaseHelper

    lateinit var spinnerBike: Spinner
    lateinit var spinnerDepartment: Spinner
    lateinit var spinnerPurpose: Spinner
    lateinit var startTimeView: TextView
    lateinit var startDateView: TextView
    lateinit var endTimeView: TextView
    lateinit var endDateView: TextView
    var rideTime: MutableMap<String, String> = mutableMapOf()
    lateinit var seekBarDistance: SeekBar
    var seekBarEndpoint: Int = 0
    lateinit var textDistance: TextView
    lateinit var inputDepartment: String
    lateinit var inputPurpose: String
    var inputBike: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ride)

        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        val bikeDao = dbHelper.getBikeRuntimeExceptionDao()
        val rideDao = dbHelper.getRideRuntimeExceptionDao()

        // setup UI
        val inputRiderView = findViewById<EditText>(R.id.edit_text_rider)
        textDistance = findViewById(R.id.textDistance)
        startTimeView = findViewById<EditText>(R.id.startTime)
        startDateView = findViewById<EditText>(R.id.startDate)
        endTimeView = findViewById<EditText>(R.id.endTime)
        endDateView = findViewById<EditText>(R.id.endDate)
        setupDatePickers()
        setupSpinners()
        setupDistanceSeekBar()


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
// "yyyy-MM-dd HH:mm:ss"
            val startDateTime =  rideTime["start_YYYY"] +
                    "-" +
                    rideTime["start_MM"] +
                    "-" +
                    rideTime["start_DD"] +
                    " " +
                    rideTime["start_hh"] +
                    ":" +
                    rideTime["start_MM"] +
                    ":00"
            val endDateTime =  rideTime["end_YYYY"] +
                    "-" +
                    rideTime["end_MM"] +
                    "-" +
                    rideTime["end_DD"] +
                    " " +
                    rideTime["end_hh"] +
                    ":" +
                    rideTime["end_MM"] +
                    ":00"


            val startUnixTime = dateToUnix(startDateTime)
            val endUnixTime = dateToUnix(endDateTime)
            Log.d("test", "Unix start"+startUnixTime.toString())
            Log.d("test", "Unix end"+endUnixTime.toString())
            val newRide = Ride(
                inputBikeId,
                inputRiderView.text.toString(),
                inputDepartment,
                startUnixTime!!,
                endUnixTime!!,
                seekBarEndpoint,
                inputPurpose
            )
            rideDao!!.create(newRide)
            val listOfRides = rideDao!!.queryForAll()
            Log.d("test", listOfRides.toString())
        }
    }
    private fun setupDatePickers() {
        startTimeView.setOnClickListener {
            TimePickerFragment(rideTime, startTimeView, endTimeView).show(supportFragmentManager, "startTimePicker")
        }

        startDateView.setOnClickListener {
            DatePickerFragment(rideTime, startDateView, endDateView).show(supportFragmentManager, "startDatePicker")
        }

        endTimeView.setOnClickListener {
            TimePickerFragment(rideTime, startTimeView, endTimeView).show(supportFragmentManager, "endTimePicker")
        }

        endDateView.setOnClickListener {
            DatePickerFragment(rideTime, startDateView, endDateView).show(supportFragmentManager, "endDatePicker")
        }
    }

    private fun setupDistanceSeekBar() {
        seekBarDistance = findViewById<SeekBar>(R.id.seekBarDistance)
        seekBarDistance.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                textDistance.text = progress.toString()
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

    fun dateToUnix(date: String): Long? {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = format.parse(date)
        var timestamp: Long? = date?.time
        return timestamp
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

    }


}