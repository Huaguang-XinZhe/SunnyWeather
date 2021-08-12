package com.liuzhihui.sunnyweather

import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.widget.Toast
import es.dmoral.toasty.Toasty
import com.liuzhihui.sunnyweather.SunnyWeatherApplication as SWA

fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(SWA.context, this, duration).show()
}

fun String.showErrorToasty(duration: Int = Toast.LENGTH_LONG) {
    Toasty.error(SWA.context, this, duration).show()
}


fun isNetWorkConnected(): Boolean {
    val manager = SWA.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo
    return networkInfo?.isAvailable ?: false
}

fun getStatusBarHeight(): Int {
    var statusBarHeight = 0
    val res = SWA.context.resources
    val resId = res.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        statusBarHeight = res.getDimensionPixelSize(resId)
    }
    return statusBarHeight
}

// 这个方法只能测得导航栏的固定值，不会随导航栏的有无而变化
fun getNavigationBarHeight(): Int {
    var navigationBarHeight = 0
    val res = SWA.context.resources
    val resId = res.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resId > 0) {
        navigationBarHeight = res.getDimensionPixelSize(resId)
    }
    return navigationBarHeight
}

// 这个方法只适用于有导航栏的情形，并且有时候会测不到
fun BaseActivity.getSoftInputHeight(): Int {
    val decorView = window.decorView
    val screenHeight = decorView.rootView.height
    LogUtil.d("screenHeight: $screenHeight")
    val rect = Rect()
    decorView.getWindowVisibleDisplayFrame(rect)
    return screenHeight - rect.bottom - getNavigationBarHeight()
}
