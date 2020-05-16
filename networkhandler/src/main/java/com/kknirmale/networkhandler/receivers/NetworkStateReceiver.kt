package com.kknirmale.networkhandler.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kknirmale.networkhandler.utils.NetworkUtil
import java.lang.ref.WeakReference


/**

Created by Ashwin Nirmale on 16 October,2019

 */
class NetworkStateReceiver : BroadcastReceiver() {

    private var internetListenerReference : WeakReference<InternetCheckListener>? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        val internetChangeListener : InternetCheckListener? = internetListenerReference?.get()

        if(context!=null) {
            internetChangeListener?.onComplete(NetworkUtil(context).isConnectedToMobileOrWifi())
            internetChangeListener?.onInternetSpeed(NetworkUtil(context).isInternetHasSpeed())
        }
    }

    /*
        Set listener on registering BroadcastReceiver
     */
    fun setInternetStateChangeListener(internetListener : InternetCheckListener){
            internetListenerReference = WeakReference(internetListener)
    }

    /*
        Remove listener on registering BroadcastReceiver
    */
    fun removeInternetStateChangeListener(){
        if (internetListenerReference != null){
            internetListenerReference?.clear()
        }
    }


    interface InternetCheckListener {
        fun onComplete(connected: Boolean)
        fun onInternetSpeed(speedType : Int)
    }


}