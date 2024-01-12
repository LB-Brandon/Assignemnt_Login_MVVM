package com.brandon.assignemnt_login_mvvm

import androidx.lifecycle.MutableLiveData

object LiveDataUtil {
    fun MutableLiveData<String>.updateStringData(newData: String) {
        this.value = newData
    }
}