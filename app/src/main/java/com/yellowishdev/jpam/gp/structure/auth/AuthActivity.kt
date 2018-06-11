package com.yellowishdev.jpam.gp.structure.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.auth.choose.ChooseAuthFragment
import com.yellowishdev.jpam.gp.structure.auth.login.LogInFragment
import com.yellowishdev.jpam.gp.structure.auth.signup.SignUpFragment
import com.yellowishdev.jpam.gp.structure.core.CoreActivity
import com.yellowishdev.jpam.gp.utils.RequestCodes


/**
 * [AuthActivity]
 * Handles Firebase Sign Up & LogIn Auth
 */

class AuthActivity : CoreActivity(), AuthContract.View {

    /*Controller*/
    override lateinit var controller: AuthContract.Controller
    override val activity: AuthActivity get() = this
    override fun getContext(): Context? = this

    /*Layout*/
    override val layoutResource: Int = R.layout.activity_auth

    override fun contentContainerId(): Int = R.id.aa_container

    /*LifeCycle*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthController(this)
        controller.create()
        setResult(Activity.RESULT_OK)
        setupUi()
    }

    override fun onStart() {
        super.onStart()
        controller.start()
    }

    override fun onResume() {
        super.onResume()
        controller.resume()
    }

    override fun onStop() {
        super.onStop()
        controller.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.destroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RequestCodes.APP_LOBBY -> {
                finish()
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    /**
     * [setupUi]
     * Handles UI changes based on Firebase registration
     * */
    private fun setupUi() {
        addFragment(contentContainerId(), ChooseAuthFragment.instance, false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        popFragment()
    }


    /**
     * Fragment Transactions
     * */
    override fun setSignUpFragment() {
        replaceFragment(contentContainerId(), SignUpFragment.instance, true, false)
    }

    override fun setLogInFragment() {
        replaceFragment(contentContainerId(), LogInFragment.instance, true, false)
    }

}