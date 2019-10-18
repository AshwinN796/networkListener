package com.kknirmale.networkhandler.config

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.kknirmale.networkhandler.listener.NetworkStateListener
import com.kknirmale.networkhandler.receivers.NetworkStateReceiver
import java.lang.ref.WeakReference


/**

    @see <a href="https://github.com/JobGetabu/DroidNet/blob/master/droidnet/src/main/java/com/droidnet/DroidNet.java">This class refer from DroidNet</a>

    Created by Ashwin Nirmale on 17 October,2019

 */
class NetworkConfig(context: Context) : NetworkStateReceiver.InternetCheckListener{

    private var configReferences : WeakReference<Context>? = null
    private var networkStateListenerWeakReferenceList : MutableList<WeakReference<NetworkStateListener>>? = null
    private var networkChangeReceiver : NetworkStateReceiver? = null
    private var isNetworkStatusRegistered = false
    private var isNetworkConnected = false

    init {
        val appContext = context.applicationContext
        configReferences = WeakReference(appContext)
        networkStateListenerWeakReferenceList = ArrayList()
    }

    companion object {
        val lock = Any()
        var mInstance: NetworkConfig? = null

        /*
            Returns instance of NetworkConfig class
         */
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
                throw IllegalStateException("Error in getting instance")
            }
            return mInstance!!
        }
    }

    /*
        Register broadcast receiver from here
     */
    private fun registerNetworkChangeReceiver(){
        val context = configReferences!!.get()
        if (context != null && !isNetworkStatusRegistered){
            networkChangeReceiver = NetworkStateReceiver()
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//            IntentFilter filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            networkChangeReceiver!!.setInternetStateChangeListener(this)
            context.registerReceiver(networkChangeReceiver, intentFilter)
            isNetworkStatusRegistered = true
        }
    }

    /*
        Unregister broadcast receiver
     */

    private fun unregisterNetworkChangeReceiver(){
        val context = configReferences!!.get()
        if (context != null && networkChangeReceiver != null && isNetworkStatusRegistered){
            context.unregisterReceiver(networkChangeReceiver)
            networkChangeReceiver!!.removeInternetStateChangeListener()
        }

        networkChangeReceiver = null
        isNetworkStatusRegistered = false
    }

    /*
        Get report of internet availability status from from listener
     */
    private fun reportInternetAvailabilityStatus(isInternetAvailable : Boolean){
        isNetworkConnected = isInternetAvailable
        if (networkStateListenerWeakReferenceList == null){
            return
        }

        val listenerIterator : MutableIterator<WeakReference<NetworkStateListener>> = networkStateListenerWeakReferenceList!!.iterator()
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

        if (networkStateListenerWeakReferenceList!!.isEmpty()){
            unregisterNetworkChangeReceiver()
        }

    }

    /*
        Attach connectivity listener to listen network state from broadcast receiver
    */
    fun addNetworkConnectivityListener(statusListener: NetworkStateListener){
        if (statusListener == null){
            return
        }

        networkStateListenerWeakReferenceList!!.add(WeakReference(statusListener))
        if (networkStateListenerWeakReferenceList!!.size == 1){
            registerNetworkChangeReceiver()
            return
        }

        reportInternetAvailabilityStatus(isNetworkConnected)

    }

    /*
        Remove connectivity listener and unregister broadcast receiver
    */
    fun removeNetworkConnectivityListener(statusListener: NetworkStateListener){
        if (statusListener == null){
            return
        }

        if (networkStateListenerWeakReferenceList!!.isEmpty()){
            return
        }

        val iterable : MutableIterator<WeakReference<NetworkStateListener>> = networkStateListenerWeakReferenceList!!.iterator()
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

            if (networkStateListenerWeakReferenceList!!.size == 0){
                unregisterNetworkChangeReceiver()
            }
        }
    }

    fun removeAllNetworkConnectivityListener(){
        if (networkStateListenerWeakReferenceList == null){
            return
        }

        val listenerIterator : MutableIterator<WeakReference<NetworkStateListener>> = networkStateListenerWeakReferenceList!!.iterator()

        while (listenerIterator.hasNext()){
            val listenerReference : WeakReference<NetworkStateListener> = listenerIterator.next()

            if (listenerReference != null){
                listenerReference.clear()
            }

            listenerIterator.remove()
        }

        unregisterNetworkChangeReceiver()
    }

    override fun onComplete(connected: Boolean) {
        if (connected){
            reportInternetAvailabilityStatus(connected)
        }else{
            reportInternetAvailabilityStatus(false)
        }
    }


//    companion object{
//        var lock : Objec
//    }

}