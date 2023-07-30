package tw.gov.taipeitravel.framework

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tw.gov.taipeitravel.R
import tw.gov.taipeitravel.Travel

open class JFragment : Fragment(), FragmentManager.OnBackStackChangedListener {

    lateinit var mCtx: Travel

    companion object {
        var previousCount = 0
        var reload = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCtx = activity?.applicationContext as Travel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentFragmentManager.addOnBackStackChangedListener(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onBackStackChanged() {
        if (isAdded) {
            var toFragment = parentFragmentManager ?.findFragmentById(R.id.frameLayout)!!
            var currentCount = parentFragmentManager?.backStackEntryCount
            if (previousCount > currentCount!! && reload)
                toFragment?.onResume()
            previousCount = currentCount
        }
    }

    fun setFramgent(framgnet: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            replace(R.id.frameLayout, framgnet)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
        requireActivity().supportFragmentManager!!.executePendingTransactions()
    }

    fun setFramgent(framgnet: Fragment, fragmentName:String) {
        requireActivity().supportFragmentManager.beginTransaction().run {
            addToBackStack(fragmentName)
            replace(R.id.frameLayout, framgnet)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
        requireActivity().supportFragmentManager!!.executePendingTransactions()
    }

    fun addFragment(targetfragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.frameLayout,targetfragment)
            addToBackStack(null)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
    }

    fun addFragment(frameLayoutID: Int, targetfragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            addToBackStack(targetfragment::class.java.simpleName) //addToBackStack(null) popBackStack failed
            add(frameLayoutID, targetfragment)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
        requireActivity().supportFragmentManager!!.executePendingTransactions()
    }

    fun removeFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            remove(fragment)
            setTransition(FragmentTransaction.TRANSIT_NONE)
            commitAllowingStateLoss()
        }
        requireActivity().supportFragmentManager!!.executePendingTransactions()
    }

    override fun onPause() {
        super.onPause()
        hideKeyBoard()
    }

    fun <T> lazyExtra(name: String, defaultValue: T): Lazy<T> {
        return lazy {
            when (defaultValue) {
                is String -> arguments?.getString(name) as T ?: defaultValue
                is Int -> arguments?.getInt(name, defaultValue) as T
                is Boolean -> arguments?.getBoolean(name, defaultValue) as T
                is List<*> -> arguments?.getSerializable(name) as T
                is Map<*, *> -> arguments?.getSerializable(name) as T
                is Set<*> -> arguments?.getSerializable("name") as T
                else -> throw IllegalArgumentException("wrong type")
            }
        }
    }

    fun LifecycleOwner.postDelayedOnLifecycle(
        duration: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: () -> Unit
    ): Job = lifecycleScope.launch(dispatcher) {
        delay(duration)
        block()
    }

    fun hideKeyBoard() {
        val imm = activity
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {
            imm.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
        }
    }

    fun popBackStack() {
        parentFragmentManager?.popBackStack()
    }

    fun popBackStack(target:String) {
        parentFragmentManager?.popBackStack(target, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}