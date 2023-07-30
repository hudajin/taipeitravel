package tw.gov.taipeitravel.framework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.Travel
import tw.gov.taipeitravel.utils.Utils

open class JAppCompatAcitivty  : AppCompatActivity() {

    lateinit var mCtx:Travel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCtx = applicationContext as Travel

        Utils.hideKeyBoard(this)
        Utils.adjustFontScale(this, resources.configuration)
        supportActionBar?.hide()

    }

    fun <T> lazyExtra(name: String, defaultValue: T): Lazy<T> {
        return lazy {
            when (defaultValue) {
                is String -> intent.getStringExtra(name) as T ?: defaultValue
                is Int -> intent.getIntExtra(name, defaultValue) as T
                is Boolean -> intent.getBooleanExtra(name, defaultValue) as T
                is List<*> -> intent.getSerializableExtra(name) as T
                is Map<*, *> -> intent.getSerializableExtra(name) as T
                is Set<*> -> intent.getSerializableExtra("name") as T
                else -> throw IllegalArgumentException("wrong type")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.hideKeyBoard(this)
    }

    fun setFramgnet(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
        supportFragmentManager.executePendingTransactions()
    }

    fun addFragment(frameLayoutID: Int, targetfragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(targetfragment::class.java.simpleName) //addToBackStack(null) popBackStack failed
            add(frameLayoutID, targetfragment)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
        supportFragmentManager!!.executePendingTransactions()
    }

}