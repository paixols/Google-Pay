package com.yellowishdev.jpam.gp.structure.core

import android.content.Context

interface BaseView<T> {

    var controller: T
    fun getContext(): Context?
    fun showError(errorName: String)
    fun showProgress()
    fun hideProgress()
    
}