package tw.gov.taipeitravel.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tw.gov.taipeitravel.Travel

object Utils {

    fun adjustFontScale(mCtx: Travel) {
        val configuration: Configuration = mCtx.resources.configuration
        if (configuration.fontScale > 1.20) {
            configuration.fontScale = 1.20.toFloat()
            val metrics: DisplayMetrics = mCtx.resources.displayMetrics
            val wm = mCtx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            mCtx.baseContext.resources.displayMetrics.setTo(metrics)
            mCtx.baseContext.resources.configuration.setTo(configuration)
        }
    }

    fun adjustFontScale(a: Activity, configuration: Configuration) {
        if (configuration.fontScale > 1.20) {
            configuration.fontScale = 1.20.toFloat()
            val metrics: DisplayMetrics = a.resources.displayMetrics
            val wm = a.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            a.baseContext.resources.displayMetrics.setTo(metrics)
            a.baseContext.resources.configuration.setTo(configuration)
        }
    }

    fun hideKeyBoard(context: Activity) {
        val imm = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {
            imm.hideSoftInputFromWindow(
                context.currentFocus!!
                    .windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
        }
    }

    fun requestAllPermission(activity: Activity) {
        val CHECK = 0
        var mPermissions = arrayOfNulls<String>(0)
        try {
            val info = activity.packageManager
                .getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
            if (info.requestedPermissions != null) {
                mPermissions = info.requestedPermissions
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        val noPermissions = ArrayList<String>()

        for (userPermission in mPermissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    userPermission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                noPermissions.add(userPermission)
            }
        }

        if (noPermissions.size != 0) {
            val permissions = noPermissions.toTypedArray()
            ActivityCompat.requestPermissions(activity, permissions, CHECK)
        }

    }

}