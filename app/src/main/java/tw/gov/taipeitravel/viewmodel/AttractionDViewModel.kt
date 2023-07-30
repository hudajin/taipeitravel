package tw.gov.taipeitravel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import tw.gov.taipeitravel.Travel
import tw.gov.taipeitravel.bean.AttractionsBean

class AttractionDViewModel (mCtx: Travel,savedStateHandle: SavedStateHandle):BaseViewModel(){

    var mCtx: Travel = mCtx

    val data: MutableLiveData<AttractionsBean.Data> = savedStateHandle.getLiveData("data")

}