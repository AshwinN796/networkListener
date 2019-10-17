package com.kknirmale.networkstatelistener

import android.app.Application
import com.kknirmale.networkhandler.config.NetworkConfig


/**

Created by Ashwin Nirmale on 17 October,2019

 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkConfig.initNetworkConfig(this)
    }
}