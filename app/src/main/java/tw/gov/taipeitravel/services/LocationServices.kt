package tw.gov.taipeitravel.services

import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat

class LocationServices {

    fun getLastKnownLocation(context: Context) : Location? {
        if(ContextCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            var mLoc: Location? = null
            var mLocManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try{
                var mCriteria = Criteria()
                mCriteria.accuracy = Criteria.ACCURACY_COARSE
                mCriteria.isAltitudeRequired = true
                mCriteria.isBearingRequired = true
                mCriteria.isSpeedRequired = true
                mCriteria.speedAccuracy = Criteria.ACCURACY_HIGH
                mCriteria.isCostAllowed = true
                mCriteria.powerRequirement = Criteria.POWER_LOW

                var gpsLocation: Location?= null
                var networkLocation: Location?= null
                if(mLocManager.isProviderEnabled((LocationManager.GPS_PROVIDER))){
                    gpsLocation = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                if(mLocManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))){
                    networkLocation = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                if (gpsLocation != null && networkLocation != null) {
                    mLoc = getBetterLocation(gpsLocation, networkLocation)
                } else if (gpsLocation != null) {
                    mLoc = gpsLocation
                } else if (networkLocation != null) {
                    mLoc = networkLocation
                }
            }catch (e:Exception){
                e.stackTrace
            }
            return mLoc;
        }else
            return null
    }

    fun getBetterLocation(newLocation: Location, currentBestLocation: Location): Location {
        val TWO_MINUTES = 1000 * 60 * 2
        if(currentBestLocation == null){
            return newLocation
        }

        var timeDelta = newLocation.time - currentBestLocation.time
        var isSignificantlyNewer = timeDelta > TWO_MINUTES
        var isSignificantlyOlder = timeDelta < -TWO_MINUTES
        var isNewer = timeDelta > 0

        if (isSignificantlyNewer) {
            Log.i("isSignificantlyNewer", "isSignificantlyNewer")
            return newLocation
        } else if (isSignificantlyOlder) {
            Log.i("isSignificantlyOlder", "isSignificantlyOlder")
            return currentBestLocation
        }

        var accuracyDelta = (newLocation.accuracy - currentBestLocation.accuracy).toInt()
        var isLessAccurate = accuracyDelta > 0
        var isMoreAccurate = accuracyDelta < 0
        var isSignificantlyLessAccurate = accuracyDelta > 200

        var isFromSameProvider = newLocation.provider?.let { currentBestLocation.provider?.let { it1 -> isSameProvider(it, it1) } }

        if (isMoreAccurate) {
            return newLocation
        } else if (isNewer && !isLessAccurate) {
            return newLocation
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider == true) {
            return newLocation
        }
        return currentBestLocation
    }

    fun isSameProvider(provider1:String,provider2:String) : Boolean{
        if(provider1 == null) {
            return provider2 == null
        }
        return provider1 == provider2;//equals
    }


}