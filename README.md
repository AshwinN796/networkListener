## NetworkListener ![](https://jitpack.io/v/AshwinN796/networkListener.svg)
NetworkLister library check the internet connection of device at runtime.
This repo has just figure out the running network state of internet on android devices.

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
implementation 'com.github.AshwinN796:networkListener:1.0.0'

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

```

#### In your activity class
```
class MainActivity : AppCompatActivity(), NetworkStateListener {

    private var networkConfig : NetworkConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	
	//create instance of network config class
        networkConfig = NetworkConfig.getInstance()
	
	//add listener for NetworkConfig
        networkConfig!!.addNetworkConnectivityListenr(this)
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
