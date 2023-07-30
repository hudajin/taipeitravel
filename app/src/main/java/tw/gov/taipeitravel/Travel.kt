package tw.gov.taipeitravel

import android.app.Activity
import android.app.Application
import android.location.Location
import android.os.Bundle
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tw.gov.taipeitravel.viewmodel.ApiService
import java.io.File
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class Travel : Application() {

    val activityLifecycleCallbacks = ActivityLifecycleCallbacks()
    lateinit var apiService: ApiService
    lateinit var okHttpClient: OkHttpClient

    @Volatile var mLoc: Location? = null

    init {
        instance = this
    }

    companion object {

        const val TIMEOUT_SEC:Long = 120
        const val BASE_URL = "https://www.travel.taipei/open-api/"

        private var instance: Travel? = null

        fun currentActivity(): Activity? {
            return instance!!.activityLifecycleCallbacks.currentActivity
        }

        fun getInstance():Travel?{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)

        setHttpClient()
        var gson = GsonBuilder().registerTypeAdapter(String::class.java, stringTypeAdapter).create()

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(ApiService::class.java)

        try {
            val filename = "logcat_"+ System.currentTimeMillis() + ".txt"
            val outputFile = File(applicationContext.externalCacheDir, filename)
            Runtime.getRuntime().exec("logcat -f " + outputFile.absolutePath)
        } catch (e: Exception) { }
    }

    private fun setHttpClient(){

        var logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(logger)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    private val stringTypeAdapter = object: TypeAdapter<String>(){
        override fun write(out: JsonWriter?, value: String?) {
            out?.value(value)
        }
        override fun read(`in`: JsonReader?): String? {
            val peek = `in`?.peek()
            if (peek == JsonToken.NULL) {
                `in`.nextNull()
                return ""
            }
            return  `in`?.nextString()
        }


    }

    class ActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks {

        var currentActivity: Activity? = null

        override fun onActivityCreated(activity: Activity, outState: Bundle?) {
            currentActivity = activity
        }

        override fun onActivityStarted(activity: Activity) {
            currentActivity = activity
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity
        }

        override fun onActivityPaused(activity: Activity) {
            currentActivity = activity
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

    }
}