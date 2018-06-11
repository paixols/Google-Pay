package com.yellowishdev.jpam.gp.structure.auth.signup

import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.auth.AuthActivity
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import com.yellowishdev.jpam.gp.model.User
import kotlinx.android.synthetic.main.fragment_signup.*

/**
 * [SignUpFragment]
 * Handles Firebase Sign Up
 * @see FirebaseDatabase
 * @see FirebaseAuth
 * */
class SignUpFragment : CoreFragment(), View.OnClickListener {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_signup

    /*Singleton pattern*/
    object Holder {
        val instance = SignUpFragment()
    }

    companion object {
        val instance: SignUpFragment by lazy { Holder.instance }
    }

    override fun onStart() {
        super.onStart()
        fs_sign_button.setOnClickListener(this)
        fs_profile_picture.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
        /*Collect Info & Sign Up*/
            fs_sign_button -> {
                collectUserInfo()?.let {
                    val fragAct = activity as AuthActivity
                    fragAct.controller.firebaseSignUp(it)
                    resetUI()
                }
            }

            fs_profile_picture -> {
                showToast("Profile picture coming soon!")
            }
        }
    }

    /**
     * [collectUserInfo]
     * Collects [User]Info & validates data input
     * */
    private fun collectUserInfo(): User? {

        //Validate Name
        if (TextUtils.isEmpty(fs_input_name.text.toString())) {
            showToast("Please provide a name")
            return null
        }

        //Validate Email
        if (TextUtils.isEmpty(fs_input_email.text.toString().toLowerCase())
                && !Patterns.EMAIL_ADDRESS.matcher(fs_input_email.text.toString()).matches()) {
            showToast("Invalid email")
            return null
        }
        //Validate Firebase Password (+6 characters)
        if (TextUtils.isEmpty(fs_input_password.text.toString()) || fs_input_password.text.count() < 6) {
            showToast("Password must have at least 6 characters")
            return null
        }
        //User
        val currentUser = User(fs_input_name.text.toString(), fs_input_email.text.toString().toLowerCase())
        currentUser.password = fs_input_password.toString()
        return currentUser
    }

    /**
     * UI
     * */
    private fun resetUI() {
        fs_input_name.text.clear()
        fs_input_email.text.clear()
        fs_input_password.text.clear()
    }

}