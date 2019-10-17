package com.kknirmale.networkstatelistener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kknirmale.networkhandler.config.NetworkConfig
import com.kknirmale.networkhandler.listener.NetworkStateListener

class MainActivity : AppCompatActivity(), NetworkStateListener {

    private var networkConfig : NetworkConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkConfig = NetworkConfig.getInstance()
        networkConfig!!.addNetworkConnectivityListenr(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConfig!!.removeNetworkConnectivityListener(this)
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        when(isConnected){
            true -> Toast.makeText(this@MainActivity,"Internet Connected",Toast.LENGTH_LONG).show()
            false -> Toast.makeText(this@MainActivity,"Internet Failed",Toast.LENGTH_LONG).show()
        }
    }
}
