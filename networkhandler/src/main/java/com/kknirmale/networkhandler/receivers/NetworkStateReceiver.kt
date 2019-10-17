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

    var b: Boolean = false

    private var internetListenerReference : WeakReference<InternetCheckListener>? = null

    companion object {
        const val TAG = "connected"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

//        b = NetworkUtils(context!!).isConnectedToMobileOrWifi()

//        if (b){
//            Toast.makeText(context,"Network connected", Toast.LENGTH_LONG).show()
//        }else{
//            Toast.makeText(context,"Network failed",Toast.LENGTH_LONG).show()
//        }

        val internetChangeListener : InternetCheckListener = internetListenerReference!!.get()!!

        internetChangeListener.onComplete(NetworkUtil(context!!).isConnectedToMobileOrWifi())

//        internetListerner?.get()!!.onComplete(b)

//        val sendIntent = Intent()
//        sendIntent.action = NetworkConstant.NETWORK_BROADCAST_ACTION
//        sendIntent.putExtra(NetworkConstant.NETWORK_CONNECTION_FLAG,NetworkUtils(context!!).isConnectedToMobileOrWifi())
////        LocalBroadcastManager.getInstance(context).sendBroadcast(sendIntent);
//        context.sendBroadcast(sendIntent)

//        Log.e(TAG,"" +NetworkUtils(context).isConnectedToMobileOrWifi())
    }

    fun setInternetChangeListener(internetListener : InternetCheckListener){

            internetListenerReference = WeakReference(internetListener)


    }

    fun removeInternetChangeListener(){
        if (internetListenerReference != null){
            internetListenerReference!!.clear()
        }
    }


    interface InternetCheckListener {
        fun onComplete(connected: Boolean)
    }


}