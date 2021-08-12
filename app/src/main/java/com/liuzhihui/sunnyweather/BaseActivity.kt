package com.liuzhihui.sunnyweather

import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.ResultReceiver
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.liuzhihui.sunnyweather.logic.NetWorkChangedReceiver

open class BaseActivity : AppCompatActivity() {

    private lateinit var receiver: NetWorkChangedReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor(Color.TRANSPARENT)
        // 动态注册网络改变广播接收器
        receiver = NetWorkChangedReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun statusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

}