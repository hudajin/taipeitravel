package tw.gov.taipeitravel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tw.gov.taipeitravel.Travel
import tw.gov.taipeitravel.bean.AttractionsBean

class AttractionViewModel  (mCtx:Travel):BaseViewModel(){

    var mCtx:Travel = mCtx

    val AttractionsBean : MutableLiveData<ApiResult<AttractionsBean>> by lazy {
        MutableLiveData<ApiResult<AttractionsBean>>()
    }

    fun doAttractionsBean(lang:String){
        viewModelScope.launch (exceptionHandler){
            AttractionsBean.value = ApiResult.Loading
            val result = getData { mCtx.apiService.doGetAttraction("application/json",lang)}
            AttractionsBean.value = ApiResult.Success(result.getOrThrow())
        }
    }

}