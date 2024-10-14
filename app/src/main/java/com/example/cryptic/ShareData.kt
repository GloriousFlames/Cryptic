package com.example.cryptic

import androidx.lifecycle.ViewModel

class ShareData : ViewModel() {
    var sharedList = ArrayList<CrypticData>()

    fun setData(list: ArrayList<CrypticData>) {
        sharedList = list
    }
}