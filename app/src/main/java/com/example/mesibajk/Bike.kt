package com.example.mesibajk

import com.j256.ormlite.field.DatabaseField

class Bike() {

    @DatabaseField(id = true)
    var id: Int = 0

    @DatabaseField
    var name: String = ""

    @DatabaseField(
        foreign = true,
        foreignAutoRefresh = true
    )
    var ride: Ride? = null

    constructor(id: Int, name: String) : this() {
        this.id = id
        this.name = name
    }

    override fun toString(): String {
        return "Bike [id = $id, name = $name, ride = $ride]"
    }

}