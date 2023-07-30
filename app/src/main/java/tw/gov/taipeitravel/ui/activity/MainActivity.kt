package tw.gov.taipeitravel.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import tw.gov.taipeitravel.databinding.ActivityMainBinding
import tw.gov.taipeitravel.framework.JAppCompatAcitivty
import tw.gov.taipeitravel.services.MyForeGroundService
import tw.gov.taipeitravel.ui.fragment.AttractionsFragment
import tw.gov.taipeitravel.utils.Utils

class MainActivity : JAppCompatAcitivty() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.adjustFontScale(this, resources.configuration)

        setFramgnet(AttractionsFragment())
    }

    fun startGPS(){
        Log.d(TAG,"startGPS")
        createchannel()
        val serviceIntent = Intent(baseContext, MyForeGroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    fun endGPS(){
        Log.d(TAG,"endGPS")
        val serviceIntent = Intent(this, MyForeGroundService::class.java)
        stopService(serviceIntent)
    }

    fun createchannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            var mChannel = NotificationChannel(
                MyForeGroundService.chinnelId,"My Channel gps",
                NotificationManager.IMPORTANCE_LOW)
            mChannel.description = "This is a  gps channel."
            mChannel.enableLights(true)
            mChannel.setShowBadge(false)
            nm.createNotificationChannel(mChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, MyForeGroundService::class.java)
        stopService(serviceIntent)

    }
}