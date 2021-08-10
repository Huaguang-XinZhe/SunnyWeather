package com.liuzhihui.sunnyweather

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarColor(resources.getColor(R.color.design_default_color_primary))
        setContentView(R.layout.activity_main)
    }
}