package com.yellowishdev.jpam.gp.structure.auth.choose

import android.view.View
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.auth.AuthActivity
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_choose_auth.*


/**
 * [ChooseAuthFragment]
 * Used for handling different kinds of Auth with Firebase
 * */
class ChooseAuthFragment : CoreFragment(), View.OnClickListener {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_choose_auth

    /*Singleton*/
    object Holder {
        val instance: ChooseAuthFragment = ChooseAuthFragment()
    }

    companion object {
        val instance: ChooseAuthFragment by lazy { Holder.instance }
    }

    override fun onStart() {
        super.onStart()
        //User selection
        fca_signup_button.setOnClickListener(this)
        fca_login_button.setOnClickListener(this)
        fca_forgot_password.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
        /*Sign Up*/
            fca_signup_button -> {
                val fragAct = activity as AuthActivity
                fragAct.controller.signUpUi()
            }
        /*Log In*/
            fca_login_button -> {
                val fragAct = activity as AuthActivity
                fragAct.controller.loginUi()
            }
        /*Forgot Password*/
            fca_forgot_password -> {
                val fragAct = activity as AuthActivity
                fragAct.showAlertWithInput("Enter email to reset password", { email ->
                    email?.let {
                        if (it.isNullOrBlank()) {
                            showToast("Invalid Email")
                        } else {
                            fragAct.controller.forgotPassword(it)
                        }
                    }
                })
            }
        }
    }

}