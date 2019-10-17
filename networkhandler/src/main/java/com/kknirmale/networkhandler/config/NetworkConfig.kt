package com.kknirmale.networkhandler.config

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.kknirmale.networkhandler.listener.NetworkStateListener
import com.kknirmale.networkhandler.receivers.NetworkStateReceiver
import java.lang.ref.WeakReference


/**

Created by Ashwin Nirmale on 17 October,2019

 */
class NetworkConfig(context: Context) : NetworkStateReceiver.InternetCheckListener{

    override fun onComplete(connected: Boolean) {
        if (connected){
            reportInternetAvialabiltyStatus(connected)
        }else{
            reportInternetAvialabiltyStatus(false)
        }
    }

    companion object {
        val lock = Any()
        var mInstance: NetworkConfig? = null


        fun initNetworkConfig(context: Context) : NetworkConfig{
            if (mInstance == null){
                synchronized(lock){
                    if (mInstance == null){
                        mInstance = NetworkConfig(context)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): NetworkConfig {
            if (mInstance == null) {
                throw IllegalStateException("Error in class")
            }
            return mInstance!!
        }
    }


    private var configReferences : WeakReference<Context>? = null
    private var networStateListenerList : MutableList<WeakReference<NetworkStateListener>>? = null
    private var networkChangeReceiver : NetworkStateReceiver? = null
    private var isNetworkStatusRegistered = false
    private var isNetworkConnected = false

    init {
        val appContext = context.applicationContext
        configReferences = WeakReference(appContext)
        networStateListenerList = ArrayList()
    }

    private fun registerNetworkChangeReceiver(){
        val context = configReferences!!.get()
        if (context != null && !isNetworkStatusRegistered){
            networkChangeReceiver = NetworkStateReceiver()
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//            IntentFilter filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            networkChangeReceiver!!.setInternetChangeListener(this)
            context.registerReceiver(networkChangeReceiver, intentFilter)
            isNetworkStatusRegistered = true
        }
    }

    private fun unregisterNetworkChangeReceiver(){
        val context = configReferences!!.get()
        if (context != null && networkChangeReceiver != null && isNetworkStatusRegistered){
            context.unregisterReceiver(networkChangeReceiver)
            networkChangeReceiver!!.removeInternetChangeListener()
        }

        networkChangeReceiver = null
        isNetworkStatusRegistered = false
    }

    private fun reportInternetAvialabiltyStatus(isInternetAvailable : Boolean){
        isNetworkConnected = isInternetAvailable
        if (networStateListenerList == null){
            return
        }

        val listenerIterator : MutableIterator<WeakReference<NetworkStateListener>> = networStateListenerList!!.iterator()
        while (listenerIterator.hasNext()){

            val listenerReference : WeakReference<NetworkStateListener> = listenerIterator.next()
            if (listenerReference == null){
                listenerIterator.remove()
                continue
            }

            val statusListener : NetworkStateListener = listenerReference.get()!!
            if (statusListener == null){
                listenerIterator.remove()
                continue
            }

            statusListener.onNetworkStatusChanged(isInternetAvailable)
        }

        if (networStateListenerList!!.isEmpty()){
            unregisterNetworkChangeReceiver()
        }

    }

    fun addNetworkConnectivityListenr(statusListener: NetworkStateListener){
        if (statusListener == null){
            return
        }

        networStateListenerList!!.add(WeakReference(statusListener))
        if (networStateListenerList!!.size == 1){
            registerNetworkChangeReceiver()
            return
        }

        reportInternetAvialabiltyStatus(isNetworkConnected)

    }

    fun removeNetworkConnectivityListener(statusListener: NetworkStateListener){
        if (statusListener == null){
            return
        }

        if (networStateListenerList!!.isEmpty()){
            return
        }

        val iterable : MutableIterator<WeakReference<NetworkStateListener>> = networStateListenerList!!.iterator()
        while (iterable.hasNext()){

            val stateReference : WeakReference<NetworkStateListener> = iterable.next()

            if (stateReference == null){
                iterable.remove()
                continue
            }

            val stateListener : NetworkStateListener = stateReference.get()!!
            if (stateListener == null){
                stateReference.clear()
                iterable.remove()
                continue
            }

            if (stateListener == statusListener){
                stateReference.clear()
                iterable.remove()
                break
            }

            if (networStateListenerList!!.size == 0){
                unregisterNetworkChangeReceiver()
            }
        }
    }


//    companion object{
//        var lock : Objec
//    }

}