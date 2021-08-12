package com.liuzhihui.sunnyweather.ui.place

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.liuzhihui.sunnyweather.R
import com.liuzhihui.sunnyweather.logic.model.Place
import com.liuzhihui.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)
        val activity = fragment.activity
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            if (activity is WeatherActivity) {
                activity.binding.drawerLayout.closeDrawers()
                activity.viewModel.apply {
                    locationLng = place.location.lng
                    locationLat = place.location.lat
                    placeName = place.name
                }
                activity.refreshWeather()
            } else {
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        holder.itemView.setOnTouchListener { it, _ ->
            if (activity is WeatherActivity) {
                activity.manager.hideSoftInputFromWindow(it.windowToken, 0)
            }
            false
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

}