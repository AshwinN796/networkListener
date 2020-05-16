package com.kknirmale.networkstatelistener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kknirmale.networkhandler.config.NetworkConfig
import com.kknirmale.networkhandler.listener.NetworkStateListener
import com.kknirmale.networkhandler.utils.NetworkConstant

class MainActivity : AppCompatActivity(), NetworkStateListener {

    private var networkConfig : NetworkConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get instance of networkConfig class
        networkConfig = NetworkConfig.getInstance()

        //add connectivity listener
        networkConfig?.addNetworkConnectivityListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //remove connectivity listener
        networkConfig?.removeNetworkConnectivityListener(this)
    }

    /*
        Do action on network status changed
        Here you can perform any action for Network state listener depending on your requirement.
     */
    override fun onNetworkStatusChanged(isConnected: Boolean) {

//        when(isConnected){
//            true -> Toast.makeText(this@MainActivity,"Internet Connected",Toast.LENGTH_LONG).show()
//            false -> Toast.makeText(this@MainActivity,"Internet Failed",Toast.LENGTH_LONG).show()
//        }
    }

    override fun onNetworkSpeedChanged(speedType: Int) {
        when(speedType) {
            NetworkConstant.WIFI_CONNECTED -> Toast.makeText(this@MainActivity,"Wifi Connected",Toast.LENGTH_LONG).show()
            NetworkConstant.FULL_SPEED_CONNECTED -> Toast.makeText(this@MainActivity,"Full Speed Connected",Toast.LENGTH_LONG).show()
            NetworkConstant.SLOW_CONNECTED ->  Toast.makeText(this@MainActivity,"No internet",Toast.LENGTH_LONG).show()
            NetworkConstant.LOW_SPEED_CONNECTED ->  Toast.makeText(this@MainActivity,"Slow internet",Toast.LENGTH_LONG).show()
        }
    }
}
