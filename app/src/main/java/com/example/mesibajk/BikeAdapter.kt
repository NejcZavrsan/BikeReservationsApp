package com.example.mesibajk

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.recyclerview.widget.RecyclerView
import com.example.mesibajk.model.Bike
import com.example.mesibajk.model.Ride

class BikeAdapter(val bikes: List<Bike>, val rides: List<Ride>, val c: Context, val myListener: OnBikeClickListener): RecyclerView.Adapter<BikeAdapter.BikeViewHolder>() {

    inner class BikeViewHolder(bikeView: View): RecyclerView.ViewHolder(bikeView), OnClickListener {
        val id = bikeView.findViewById<TextView>(R.id.bike_ID)
        val name = bikeView.findViewById<TextView>(R.id.bike_name)
        val occupancy = bikeView.findViewById<TextView>(R.id.bike_occupancy)
        val icon = bikeView.findViewById<ImageView>(R.id.bike_icon)
        init {
            bikeView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            myListener.onBikeViewClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeViewHolder {
        val bikeView = LayoutInflater.from(parent.context).inflate(R.layout.bike_view_item, parent, false)
        return BikeViewHolder(bikeView)
    }

    override fun getItemCount(): Int {
        return bikes.size
    }

    override fun onBindViewHolder(holder: BikeViewHolder, position: Int) {
        val currentBike = bikes[position]

        holder.id.text = currentBike.id.toString()
        holder.name.text = currentBike.name

        // determine bike occupancy
        val currentUnixTime = System.currentTimeMillis()
        Log.d("test", "CurrentUnixTime: $currentUnixTime")

        var currentBikeOccupied = false
        for (ride in rides) {
            if (ride.id_bike == position + 1) {
                if (currentUnixTime in ride.start_time!! until ride.end_time!!) {
                    currentBikeOccupied = true
                    // TODO: end for loop here
                }
            }
        }
        if (currentBikeOccupied) {
            holder.occupancy.text = c.getString(R.string.occupied)
            holder.icon.setImageResource(R.drawable.baseline_circle_orange)
        } else {
            holder.occupancy.text = c.getString(R.string.frej)
            holder.icon.setImageResource(R.drawable.baseline_circle_green)
        }
    }

    interface OnBikeClickListener {

        fun onBikeViewClick(position: Int)
    }
}