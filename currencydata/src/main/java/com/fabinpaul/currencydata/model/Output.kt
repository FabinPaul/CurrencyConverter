package com.fabinpaul.currencydata.model

sealed class Output<out R> {

    data class Success<out T>(val data: T) : Output<T>()
    data class Error(val exception: Exception) : Output<Nothing>()
    object Loading : Output<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val Output<*>.succeeded
    get() = this is Output.Success && data != null