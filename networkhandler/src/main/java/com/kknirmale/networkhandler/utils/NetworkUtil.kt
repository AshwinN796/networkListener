package com.kknirmale.networkhandler.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager


/**

Created by Ashwin Nirmale on 16 October,2019

 */
class NetworkUtil() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var context : Context

    constructor(context: Context) : this(){
        this.context = context
    }

    private val networkInfo: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnectedToMobileOrWifi(): Boolean {

        var isConnectedToWifi = false
        var isConnectedToMobile = false
        var isInternetFast = false

        val info = NetworkUtil(context).networkInfo.activeNetworkInfo

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = NetworkUtil(context).networkInfo.activeNetwork
            val networkCapabilities = NetworkUtil(context).networkInfo.getNetworkCapabilities(network)

            if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isConnectedToWifi = true
            }
            if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isConnectedToMobile = true
            }

            return isConnectedToWifi || isConnectedToMobile
        } else {
            if (info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI) {
                isConnectedToWifi = true
            }

            if (info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE) {
                isConnectedToMobile = true
            }
            if (info != null && info.isConnected && isInternetFast(info.type, info.subtype)) {
                isInternetFast = true
            }

            return isConnectedToWifi || isConnectedToMobile || isInternetFast
        }

    }

    fun isInternetHasSpeed() : Int {
        val info = NetworkUtil(context).networkInfo.activeNetworkInfo
        return if (isConnectedToMobileOrWifi()) {
            return when (info?.type) {
                ConnectivityManager.TYPE_WIFI -> NetworkConstant.WIFI_CONNECTED
                ConnectivityManager.TYPE_MOBILE -> when (info.subtype) {
                    TelephonyManager.NETWORK_TYPE_1xRTT -> NetworkConstant.SLOW_CONNECTED // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> NetworkConstant.SLOW_CONNECTED // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> NetworkConstant.SLOW_CONNECTED // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> NetworkConstant.LOW_SPEED_CONNECTED // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> NetworkConstant.LOW_SPEED_CONNECTED // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_GPRS -> NetworkConstant.SLOW_CONNECTED // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> NetworkConstant.FULL_SPEED_CONNECTED // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> NetworkConstant.LOW_SPEED_CONNECTED // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> NetworkConstant.FULL_SPEED_CONNECTED // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_UMTS -> NetworkConstant.LOW_SPEED_CONNECTED // ~ 400-7000 kbps
                    /*
                     * Above API level 7, make sure to set android:targetSdkVersion
                     * to appropriate level to use these
                     * Here the Minimum API Level is 22 so no need
                     */
                    TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                    -> NetworkConstant.FULL_SPEED_CONNECTED // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                    -> NetworkConstant.FULL_SPEED_CONNECTED // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                    -> NetworkConstant.FULL_SPEED_CONNECTED // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                    -> NetworkConstant.SLOW_CONNECTED // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_LTE // API level 11
                    -> NetworkConstant.FULL_SPEED_CONNECTED // ~ 10+ Mbps
                    // Unknown
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> NetworkConstant.SLOW_CONNECTED
                    else -> NetworkConstant.SLOW_CONNECTED
                }
                else -> NetworkConstant.SLOW_CONNECTED
            }
        } else {
            NetworkConstant.SLOW_CONNECTED
        }
    }

    private fun isInternetFast(type: Int, subType: Int): Boolean {
        return when (type) {
            ConnectivityManager.TYPE_WIFI -> true
            ConnectivityManager.TYPE_MOBILE -> when (subType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 * Here the Minimum API Level is 22 so no need
                 */
                TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                -> true // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                -> true // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                -> true // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                -> false // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE // API level 11
                -> true // ~ 10+ Mbps
                // Unknown
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
            else -> false
        }
    }

}