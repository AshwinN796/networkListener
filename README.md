# NetworkListener ![](https://jitpack.io/v/AshwinN796/networkListener.svg)

NetworkLister is the library to check the internet connection status of device at runtime.


## New Features!
  - Now detect runtime internet speed status

### Installation
#### Project level gradle

```Gradle
allprojects {
 repositories {
    maven { url 'https://jitpack.io' }
 }
}
```

#### Module level gradle
```Gradle
implementation 'com.github.AshwinN796:networkListener:1.1.1'
```

##### Implementation
Application class :

```Kotlin
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
```
Activity class :

```Kotlin
class MainActivity : AppCompatActivity(), NetworkStateListener {

    private var networkConfig : NetworkConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get instance of networkConfig class
        networkConfig = NetworkConfig.getInstance()

        //add connectivity listener
        networkConfig!!.addNetworkConnectivityListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        //remove connectivity listener
        networkConfig!!.removeNetworkConnectivityListener(this)
    }

    /*
        Do action on network status changed
        Here you can perform any action for Network state listener depending on your requirement.
     */
    override fun onNetworkStatusChanged(isConnected: Boolean) {

//        when(isConnected){
//            true -> {//make your action}
//            false -> {}
//        }
    }

    override fun onNetworkSpeedChanged(speedType: Int) {
        when(speedType) {
            NetworkConstant.WIFI_CONNECTED -> {//make your action}
            NetworkConstant.FULL_SPEED_CONNECTED -> {}
            NetworkConstant.SLOW_CONNECTED ->  {}
            NetworkConstant.LOW_SPEED_CONNECTED -> {}
        }
    }
}

```
