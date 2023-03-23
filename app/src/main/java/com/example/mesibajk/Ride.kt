package com.example.mesibajk

import com.j256.ormlite.field.DatabaseField

class Ride() {

    @DatabaseField(generatedId = true, unique = true)
    var id: Int? = 0

    @DatabaseField
    var id_bike: Int = 0

    @DatabaseField
    var user: String? = ""

    @DatabaseField
    var department: String? = ""

    @DatabaseField
    var start_time: Int? = 0

    @DatabaseField
    var end_time: Int? = 0

    @DatabaseField
    var distance: Int? = 0

    @DatabaseField
    var purpose: String? = ""

    constructor(id_bike: Int, user: String, department: String, start_time: Int, end_time: Int, distance: Int, purpose: String) : this() {
        this.id_bike = id_bike
        this.user = user
        this.department = department
        this.start_time = start_time
        this.end_time = end_time
        this.distance = distance
        this.purpose = purpose
    }

    override fun toString(): String {
        return "Ride [id = $id, " +
                "id_bike = $id_bike, " +
                "user = $user, " +
                "department = $department, " +
                "start_time = $start_time, " +
                "end_time = $end_time, " +
                "distance = $distance, " +
                "purpose = $purpose] "
    }

}