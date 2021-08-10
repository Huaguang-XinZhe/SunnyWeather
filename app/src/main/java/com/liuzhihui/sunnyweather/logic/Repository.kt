package com.liuzhihui.sunnyweather.logic

import androidx.lifecycle.liveData
import com.liuzhihui.sunnyweather.logic.model.Weather
import com.liuzhihui.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object Repository {

    fun searchPlaces(query: String) = liveData {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun refreshWeather(lng: String, lat: String) = liveData {
        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}\n" +
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

}