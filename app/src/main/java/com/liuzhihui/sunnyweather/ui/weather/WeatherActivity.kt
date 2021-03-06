package com.liuzhihui.sunnyweather.ui.weather

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.liuzhihui.sunnyweather.*
import com.liuzhihui.sunnyweather.databinding.ActivityWeatherBinding
import com.liuzhihui.sunnyweather.databinding.ForecastItemBinding
import com.liuzhihui.sunnyweather.logic.getSky
import com.liuzhihui.sunnyweather.logic.model.Weather
import com.xiasuhuei321.loadingdialog.view.LoadingDialog
import java.util.*

class WeatherActivity : BaseActivity() {

    lateinit var binding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    // 这个不能直接赋值：System services not available to Activities before onCreate()
    private val ld by lazy { LoadingDialog(this) }

    val manager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    private val searchEdit: EditText by lazy { findViewById(R.id.searchPlaceEdit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.apply {
            locationLng = intent.getStringExtra("location_lng") ?: ""
            locationLat = intent.getStringExtra("location_lat") ?: ""
            placeName = intent.getStringExtra("place_name") ?: ""
            ld.setLoadingText("加载中……")
                .setInterceptBack(true)
                .setLoadSpeed(LoadingDialog.Speed.SPEED_TWO)
                .show()
            refreshWeather(locationLng, locationLat)
            weatherLiveData.observe(this@WeatherActivity) { result ->
                val weather = result.getOrNull()
                ld.close()
                if (weather != null) {
                    showWeatherInfo(weather)
                } else {
                    "无法成功获取天气信息".showToast()
                    result.exceptionOrNull()?.printStackTrace()
                }
                binding.swipeRefresh.isRefreshing = false
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.setColorSchemeResources(R.color.teal_200)
            refreshWeather()
        }
        binding.now.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                manager.showSoftInput(searchEdit, InputMethodManager.SHOW_FORCED)
            }

            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerClosed(drawerView: View) {
                manager.hideSoftInputFromWindow(drawerView.windowToken, 0)
                searchEdit.setText("")
            }
        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充 now.xml 布局中的数据
        binding.now.apply {
            placeName.text = viewModel.placeName
            val currentTempText = "${realtime.temperature.toInt()} ℃"
            currentTemp.text = currentTempText
            currentSky.text = getSky(realtime.skycon).info
            val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
            currentAQI.text = currentPM25Text
            nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        }
        // 填充 forecast.xml 中的数据
        binding.forecast.apply {
            forecastLayout.removeAllViews()
            for (i in daily.skycon.indices) {
                val skycon = daily.skycon[i]
                val temperature = daily.temperature[i]
                val fcItemBinding = ForecastItemBinding.inflate(layoutInflater)
                fcItemBinding.apply {
//                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    dateInfo.text = simpleDateFormat.format(skycon.date)
                    val dateText = skycon.date.substring(0, 10)
                    when (i) {
                        0 -> dateInfo.text = "今天"
                        1 -> dateInfo.text = "明天"
                        2 -> dateInfo.text = "后天"
                        else -> dateInfo.text = dateText
                    }
                    val sky = getSky(skycon.value)
                    skyIcon.setImageResource(sky.icon)
                    skyInfo.text = sky.info
                    val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                    temperatureInfo.text = tempText
                }
                forecastLayout.addView(fcItemBinding.root)
            }
        }
        // 填充 life_index.xml 布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.apply {
            coldRiskText.text = lifeIndex.coldRisk[0].desc
            dressingText.text = lifeIndex.dressing[0].desc
            ultravioletText.text = lifeIndex.ultraviolet[0].desc
            carWashingText.text = lifeIndex.carWashing[0].desc
        }
        // 显示 weatherLayout
        binding.weatherLayout.visibility = View.VISIBLE
    }

}
