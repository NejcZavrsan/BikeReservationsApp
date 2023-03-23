package com.example.mesibajk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.RuntimeExceptionDao

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var sharedPreferences: SharedPreferences
    lateinit var bikeDao: RuntimeExceptionDao<Bike, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check first launch info
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val isFirstLaunch: Boolean = sharedPreferences.getBoolean(FIRST_LAUNCH, true)

        // Instantiate helper and dao
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
        bikeDao = dbHelper.getBikeRuntimeExceptionDao()!!

        if (isFirstLaunch) {
            createSevenBikes()
            Log.d("test", "Was first launch")
        }
        Log.d("test", "After if first launch statement")
        doBikeDataStuff()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intentMakeReservation = Intent(this, AddSimpleRideActivity::class.java)
            startActivity(intentMakeReservation)

        }

        // Set first launch to false
        val editor = sharedPreferences.edit()
        editor.putBoolean(FIRST_LAUNCH, false)
        editor.commit()
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

    private fun doBikeDataStuff() {

        val listOfBikes = bikeDao.queryForAll()
        val bike1 = bikeDao.queryForEq("id", "1")
        val bike2 = bikeDao.queryForEq("id", "2")
        val bike3 = bikeDao.queryForEq("id", "3")
        val bike4 = bikeDao.queryForEq("id", "4")
        val bike5 = bikeDao.queryForEq("id", "5")
        val bike6 = bikeDao.queryForEq("id", "6")
        val bike7 = bikeDao.queryForEq("id", "7")

        Log.d("test", listOfBikes.toString())

        val bike1Name = findViewById<TextView>(R.id.bike1_name)
        val bike2Name = findViewById<TextView>(R.id.bike2_name)
        val bike3Name = findViewById<TextView>(R.id.bike3_name)
        val bike4Name = findViewById<TextView>(R.id.bike4_name)
        val bike5Name = findViewById<TextView>(R.id.bike5_name)
        val bike6Name = findViewById<TextView>(R.id.bike6_name)
        val bike7Name = findViewById<TextView>(R.id.bike7_name)

        bike1Name.text = bike1[0].name
        bike2Name.text = bike2[0].name
        bike3Name.text = bike3[0].name
        bike4Name.text = bike4[0].name
        bike5Name.text = bike5[0].name
        bike6Name.text = bike6[0].name
        bike7Name.text = bike7[0].name



        OpenHelperManager.releaseHelper()
    }

    companion object {
        const val MyPREFERENCES = "shared_preferences"
        const val FIRST_LAUNCH = "first_launch"
    }
}