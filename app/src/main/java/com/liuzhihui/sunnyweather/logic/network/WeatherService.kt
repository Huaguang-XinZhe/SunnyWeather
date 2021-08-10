package com.liuzhihui.sunnyweather.logic.network

import com.liuzhihui.sunnyweather.logic.model.DailyResponse
import com.liuzhihui.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import com.liuzhihui.sunnyweather.SunnyWeatherApplication as SWA
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.5/${SWA.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String,
                           @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.5/${SWA.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String,
                        @Path("lat") lat: String): Call<DailyResponse>

}