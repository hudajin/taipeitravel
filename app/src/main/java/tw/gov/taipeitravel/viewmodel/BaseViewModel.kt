package tw.gov.taipeitravel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tw.gov.taipeitravel.Travel
import tw.gov.taipeitravel.utils.JDialog
import java.io.PrintWriter
import java.io.StringWriter

open class BaseViewModel() : ViewModel() {

    suspend fun <T: Any> getData(requestFunc: suspend () -> T) = withContext(Dispatchers.IO) {
        return@withContext try {
            Result.success<T>(requestFunc.invoke())
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
    }

    fun fail(e:Throwable?) {
        JDialog.cancelLoading()
        Travel.currentActivity().let {
            try {
                var msg = e?.message ?: e?.let { getPrintStackTrace(e) }
                JDialog.showMessage(it!!,"提示訊息",msg)
            } catch (e: Exception) {
                JDialog.showMessage(it!!,"提示訊息",e.fillInStackTrace().toString())
            }
        }
    }

    private fun getPrintStackTrace(e:Throwable):String{
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        return pw.toString().takeUnless { it.length >= 50 }?.apply {
            substring(0,50)
        }?:pw.toString()
    }

    val stringResult : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        fail(throwable)
    }

}