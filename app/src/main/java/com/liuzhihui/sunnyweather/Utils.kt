package com.liuzhihui.sunnyweather

import android.widget.Toast
import com.liuzhihui.sunnyweather.SunnyWeatherApplication as SWA

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(SWA.context, this, duration).show()
}