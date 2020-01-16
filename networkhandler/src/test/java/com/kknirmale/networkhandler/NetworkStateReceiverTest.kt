package com.kknirmale.networkhandler

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowApplication

/**
 * Created by Ashwin Nirmale on 2019-12-10.
 */

@RunWith(RobolectricTestRunner::class)
class NetworkStateReceiverTest {

    private lateinit var context : Context

    @Before
    fun setup(){
        context = RuntimeEnvironment.systemContext

    }

    //test broadcast receiver with intent value
    @Test
    fun validateIntentHandlingToBroadcast(){
        val intentValue = Intent("android.net.conn.CONNECTIVITY_CHANGE")

//        val shadowApplication = ShadowApplication.getInstance().
//        val context : Context = RuntimeEnvironment.systemContext
//        Assert.assertTrue(context.rece)

        val shadowApplication = ShadowApplication()
//        val shadowApplication : ShadowApplication = Shadows.shadowOf((Application) ApplicationProvider!!.getApplicationContext())

        Assert.assertTrue(shadowApplication.hasReceiverForIntent(intentValue))

    }
}