package com.yellowishdev.jpam.gp.structure.auth.login

import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.auth.AuthActivity
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import com.yellowishdev.jpam.gp.model.User
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * [LogInFragment]
 * Handles Firebase Login Auth
 * */
class LogInFragment : CoreFragment(), View.OnClickListener {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_login

    object Holder {
        val instance = LogInFragment()
    }

    companion object {
        val instance: LogInFragment by lazy { Holder.instance }
    }

    /*LifeCycle*/

    override fun onStart() {
        super.onStart()
        fl_sign_button.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            fl_sign_button -> {
                validateSignIn()?.let { currentUser ->
                    val fragAct = activity as AuthActivity
                    fragAct.controller.firebaseLogIn(currentUser)
                    resetUI()
                }
            }
        }
    }

    /**
     * Validate information
     * */
    private fun validateSignIn(): User? {
        //Email validation
        if (TextUtils.isEmpty(fl_input_email.text.toString()) && Patterns.EMAIL_ADDRESS.matcher(fl_input_email.text.toString()).matches()) {
            showToast("Invalid email")
            return null
        }
        //Password Validation
        if (TextUtils.isEmpty(fl_input_password.text.toString()) || fl_input_password.text.toString().count() < 6) {
            showToast("Password must have at least 6 characters")
            return null
        }
        //User
        val currentUser = User(null, fl_input_email.text.toString().toLowerCase())
        currentUser.password = fl_input_password.text.toString()
        return currentUser
    }

    /**
     * UI
     * */
    private fun resetUI () {
        fl_input_email.text.clear()
        fl_input_password.text.clear()
    }

}