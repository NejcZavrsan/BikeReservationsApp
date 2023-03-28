package com.example.mesibajk

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mesibajk.model.Bike

class BikeAdapter(val bikes: List<Bike>, val myListener: OnBikeClickListener): RecyclerView.Adapter<BikeAdapter.BikeViewHolder>() {

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
        holder.occupancy.text = "todo"
        holder.icon.setImageResource(R.drawable.baseline_circle_green)
    }

    interface OnBikeClickListener {

        fun onBikeViewClick(position: Int)
    }
}