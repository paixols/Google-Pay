package com.yellowishdev.jpam.gp.structure.lobby.empty

import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.core.CoreFragment

/**
 * [EmptyFragment]
 * Used to avoid replacing fragment blinking in UI in zero state
 * */
class EmptyFragment : CoreFragment() {
    override fun layoutId(): Int = R.layout.fragment_empty

    object Holder {
        val instance = EmptyFragment()
    }

    companion object {
        val instance: EmptyFragment by lazy { Holder.instance }
    }
}