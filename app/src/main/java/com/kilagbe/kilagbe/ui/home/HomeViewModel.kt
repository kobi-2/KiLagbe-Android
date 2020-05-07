package com.kilagbe.kilagbe.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilagbe.kilagbe.R


/**
 *   THIS VIWEMODEL HAS NOT BEEN INCLUDED YET...
 */


class HomeViewModel : ViewModel() {

    //    variables
    private lateinit var  mNames : MutableLiveData<ArrayList<String>>
    private lateinit var mNames2 : MutableLiveData<ArrayList<String>>


    var _names: ArrayList<String> = ArrayList()
    var _names2: ArrayList<String> = ArrayList()

    //    not sure if this add thing works
    fun setValues() {
        _names.add("Undergraduate")
        _names.add("Post Graduate")
        _names.add("English Medium")
        _names.add("NCTB")
        _names.add("Abroad")

        _names2.add("Book 1")
        _names2.add("Book 2")
        _names2.add("Book 3")
        _names2.add("Book 4")
        _names2.add("Book 5")
        _names2.add("Book 6")
        _names2.add("Book 7")
        _names2.add("Book 8")
        _names2.add("Book 9")
        _names2.add("Book 10")


        mNames = MutableLiveData(_names)
        mNames2 = MutableLiveData(_names2)

    }



    fun getNames() : LiveData<ArrayList<String>>{
        return  mNames
    }

    fun getNames2() : LiveData<ArrayList<String>>{
        return  mNames2
    }



/*
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
*/


}


