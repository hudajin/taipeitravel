package tw.gov.taipeitravel.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.Travel


class MyForeGroundService: Service {

    private lateinit var mLocationManager: LocationManager
    private lateinit var mServiceLooper: Looper
    private lateinit var mServiceHandler: ServiceHandler
    private lateinit var mCtx: Travel
    companion object {
        var chinnelId = "GPSChannel"
    }
    private val mLocationServices = LocationServices()
    private var mListenerMask = 0
    private val LISTENER_MASK_LOCATION = 1
    private var SATELLITE_COUNT = ""
    constructor() : super()

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            val notification = getNotification("GPS定位中")
            startForeground(msg.arg1, notification)  //not sure what the ID needs to be.
            while (true) {
                synchronized(this) {
                    try {
                        Thread.sleep(30 * 1000)
                    } catch (e: Exception) {
                    }
                }

                if (mCtx.mLoc != null) {
                    val info = "GPS=" +mCtx.mLoc?.latitude + "" + "/" + mCtx.mLoc?.longitude + " SATELLITE_COUNT=" + SATELLITE_COUNT
                    GetGPS(info)
                }
            }
        }
    }

    internal val mHandler = Handler()
    internal fun GetGPS(text: CharSequence) {
        mHandler.post {
            Log.d("經緯度", text.toString())

        }
    }

    override fun onCreate() {
        mCtx = applicationContext as Travel
        //mCtx.isOnduty = true //先測試用
        mCtx.mLoc = mLocationServices.getLastKnownLocation(mCtx)
        if (mCtx.mLoc == null) {
            mCtx.mLoc = Location("dummyprovider")
            mCtx.mLoc!!.setLatitude(24.2204731)
            mCtx.mLoc!!.setLongitude(120.6756873)
        }
        registListener()
        val thread = HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND)
        thread.start()
        mServiceLooper = thread.looper
        mServiceHandler = ServiceHandler(mServiceLooper!!)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //小於七的沒有提供衛星訊號功能
                mLocationManager.registerGnssStatusCallback(object : GnssStatus.Callback() {
                    override fun onStarted() {
                        super.onStarted()
                        SATELLITE_COUNT = "GNSS系統啟動"
                    }

                    override fun onStopped() {
                        super.onStopped()
                        SATELLITE_COUNT = "GNSS系統停止"
                    }

                    override fun onFirstFix(ttffMillis: Int) {
                        super.onFirstFix(ttffMillis)
                        SATELLITE_COUNT = "GNSS系統首次定位成功"
                    }

                    override fun onSatelliteStatusChanged(status: GnssStatus) {
                        super.onSatelliteStatusChanged(status)
                        SATELLITE_COUNT = status.satelliteCount.toString()
                    }
                })
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun registListener() {
        //中第一個參數可以使用GPS_PROVIDER或者NETWORK_PROVIDER，第二個參數是 獲取GPS資訊的時間單位ms，設置時間太小的話會增加耗電，第三個參數是觸發listener位址改變的位置變化，單位為米
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                mLocationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                mLocationListener
            )
            mListenerMask = mListenerMask or LISTENER_MASK_LOCATION
        }
    }

    private fun unregistListener() {
        if (mListenerMask and LISTENER_MASK_LOCATION != 0) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(mLocationListener)
            mListenerMask = mListenerMask and LISTENER_MASK_LOCATION.inv()
        }
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            mCtx!!.mLoc = location
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val msg = mServiceHandler.obtainMessage()
        msg.arg1 = startId//needed for stop.

        if (intent != null) {
            msg.data = intent.extras
            mServiceHandler.sendMessage(msg)
        } else {
            Toast.makeText(
                this@MyForeGroundService,
                "The Intent to start is null?!",
                Toast.LENGTH_SHORT
            ).show()
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {

        unregistListener()
    }

    fun getNotification(message: String): Notification {

        return NotificationCompat.Builder(applicationContext,
            chinnelId
        )
            .setSmallIcon(R.drawable.baseline_rss_feed_24)
            .setOngoing(true)
            .setChannelId(chinnelId)
            .setContentTitle("GPS服務")
            .setContentText(message)
            .build()
    }

}