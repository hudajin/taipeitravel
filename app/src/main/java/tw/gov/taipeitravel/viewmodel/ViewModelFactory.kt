package tw.gov.taipeitravel.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tw.gov.taipeitravel.Travel
class ViewModelFactory (private val mCtx: Travel, private val state: SavedStateHandle) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AttractionViewModel::class.java) -> {
                return AttractionViewModel(mCtx) as T
            }
            modelClass.isAssignableFrom(AttractionDViewModel::class.java) -> {
                return AttractionDViewModel(mCtx,state) as T
            }
        }
        throw IllegalArgumentException("Unknown " + modelClass.simpleName + " name")
    }
}