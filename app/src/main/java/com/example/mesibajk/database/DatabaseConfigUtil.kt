package com.example.mesibajk.database

import com.example.mesibajk.model.Bike
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil

class DatabaseConfigUtil: OrmLiteConfigUtil() {

    val classes = arrayOf(Bike::class.java)

    fun main() {
        writeConfigFile("ormlite_config.txt", classes)
    }
}