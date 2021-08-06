package com.liuzhihui.sunnyweather.logic.network

import com.liuzhihui.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import com.liuzhihui.sunnyweather.SunnyWeatherApplication as SWA
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("v2/place?token=${SWA.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}