package com.kknirmale.networkhandler.listener


/**

Created by Ashwin Nirmale on 17 October,2019

 */
interface NetworkStateListener {

    fun onNetworkStatusChanged(isConnected : Boolean)

    fun onNetworkSpeedChanged(speedType : Int)
}