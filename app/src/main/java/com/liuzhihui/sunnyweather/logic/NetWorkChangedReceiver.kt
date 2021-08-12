package com.liuzhihui.sunnyweather.logic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.liuzhihui.sunnyweather.isNetWorkConnected
import com.liuzhihui.sunnyweather.showErrorToasty
import com.liuzhihui.sunnyweather.showToast

class NetWorkChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!isNetWorkConnected()) {
            "当前网络未连接，无法获取并更新天气信息".showErrorToasty()
        }
    }

}