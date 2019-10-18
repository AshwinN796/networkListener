package com.kknirmale.networkstatelistener

import android.app.Application
import com.kknirmale.networkhandler.config.NetworkConfig


/**

Created by Ashwin Nirmale on 17 October,2019

 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //init NetworkConfig
        NetworkConfig.initNetworkConfig(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //Remove all listeners while on low memory
        NetworkConfig.getInstance().removeAllNetworkConnectivityListener()

    }
}