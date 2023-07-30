package tw.gov.taipeitravel.viewmodel

sealed class ApiResult<out R> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    object Loading : ApiResult<Nothing>()
    object Error : ApiResult<Nothing>()
}