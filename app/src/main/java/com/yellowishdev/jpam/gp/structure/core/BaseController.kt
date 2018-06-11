package com.yellowishdev.jpam.gp.structure.core

interface BaseController {
    /*LifeCycle*/
    fun create()
    fun start()
    fun resume()
    fun pause()
    fun stop()
    fun destroy()
}