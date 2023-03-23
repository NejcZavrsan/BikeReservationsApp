package com.example.exercisemesibike1

import com.example.mesibajk.Bike
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil

class DatabaseConfigUtil: OrmLiteConfigUtil() {

    val classes = arrayOf(Bike::class.java)

    fun main() {
        writeConfigFile("ormlite_config.txt", classes)
    }
}