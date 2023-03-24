package com.example.mesibajk.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.mesibajk.model.Bike
import com.example.mesibajk.R
import com.example.mesibajk.model.Ride
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException

class DatabaseHelper(
    context: Context,
) : OrmLiteSqliteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION,
    R.raw.ormlite_config
) {

    private var bikeDao: Dao<Bike, Int>? = null
    private var bikeRuntimeExceptionDao: RuntimeExceptionDao<Bike, Int>? = null
    private var rideDao: Dao<Ride, Int>? = null
    private var rideRuntimeExceptionDao: RuntimeExceptionDao<Ride, Int>? = null

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Ride::class.java)
            TableUtils.createTableIfNotExists(connectionSource, Bike::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(
        database: SQLiteDatabase?,
        connectionSource: ConnectionSource?,
        oldVersion: Int,
        newVersion: Int
    ) {
        try {
            TableUtils.dropTable<Bike, Int>(connectionSource, Bike::class.java, true)
            TableUtils.dropTable<Ride, Int>(connectionSource, Ride::class.java, true)
            onCreate(database, connectionSource)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getBikeDao(): Dao<Bike, Int>? {
        if (bikeDao == null) {
            bikeDao = getDao(Bike::class.java)
        }
        return bikeDao
    }

    fun getBikeRuntimeExceptionDao(): RuntimeExceptionDao<Bike, Int>? {
        if (bikeRuntimeExceptionDao == null) {
            bikeRuntimeExceptionDao = getRuntimeExceptionDao(Bike::class.java)
        }
        return bikeRuntimeExceptionDao
    }

    fun getRideDao(): Dao<Ride, Int>? {
        if (rideDao == null) {
            rideDao = getDao(Ride::class.java)
        }
        return rideDao
    }

    fun getRideRuntimeExceptionDao(): RuntimeExceptionDao<Ride, Int>? {
        if (rideRuntimeExceptionDao == null) {
            rideRuntimeExceptionDao = getRuntimeExceptionDao(Ride::class.java)
        }
        return rideRuntimeExceptionDao
    }

    companion object {
        const val DATABASE_NAME: String = "mesi_bike.db"
        const val DATABASE_VERSION: Int = 1
    }
}