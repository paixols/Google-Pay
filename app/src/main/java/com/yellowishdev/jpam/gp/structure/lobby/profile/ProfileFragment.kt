package com.yellowishdev.jpam.gp.structure.lobby.profile

import android.os.Bundle
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.model.User
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import com.yellowishdev.jpam.gp.utils.DatabaseNodes
import com.yellowishdev.jpam.gp.utils.DatabaseUserNodes
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * [ProfileFragment]
 * Display Profile information stored in Firebase
 * */
class ProfileFragment : CoreFragment() {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_profile

    companion object {
        fun create(user: User): ProfileFragment {
            val args = Bundle()
            args.putSerializable(DatabaseNodes.user, user)
            val frag = ProfileFragment()
            frag.arguments = args
            return frag
        }
    }

    /*LifeCycle*/
    override fun onStart() {
        super.onStart()
        setupUserInfo()
    }

    /**
     * Recycler
     * */
    private fun setupUserInfo() {
        //Name
        arguments?.getSerializable(DatabaseNodes.user)?.let {
            var user = it as User
            user.name?.let {
                fp_name_value.text = it

            }
            user.email?.let {
                fp_email_value.text = it

            }
        }

    }

}