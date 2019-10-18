## NetworkListener ![](https://jitpack.io/v/AshwinN796/networkListener.svg)
NetworkLister is the library to check the internet connection status of device at runtime.

## SetUp
##### project level gradle
```
allprojects {
 repositories {
    ...
    maven { url 'https://jitpack.io' }
 }
}

```

#### module level gradle
```
implementation 'com.github.AshwinN796:networkListener:1.0.1'

```

## How to use

#### In your application class
```
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
	//init network class here
        NetworkConfig.initNetworkConfig(this)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //Remove all listeners while on low memory
        NetworkConfig.getInstance().removeAllNetworkConnectivityListener()

        }
    }

```

#### In your activity class
```
class MainActivity : AppCompatActivity(), NetworkStateListener {

    private var networkConfig : NetworkConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	
	//get instance of networkConfig class
        networkConfig = NetworkConfig.getInstance()
	
	//add listener for NetworkConfig
        networkConfig!!.addNetworkConnectivityListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
	//remove listener from NetworkConfig
        networkConfig!!.removeNetworkConnectivityListener(this)
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        when(isConnected){
            true -> Toast.makeText(this@MainActivity,"Internet Connected",Toast.LENGTH_LONG).show()
            false -> Toast.makeText(this@MainActivity,"Internet Failed",Toast.LENGTH_LONG).show()
        }
    }
}

```
