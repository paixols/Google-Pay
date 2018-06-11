package com.yellowishdev.jpam.gp.structure.auth

import com.yellowishdev.jpam.gp.structure.core.BaseController
import com.yellowishdev.jpam.gp.structure.core.BaseView
import com.yellowishdev.jpam.gp.model.User


interface AuthContract {

    interface View : BaseView<Controller> {

        val activity: AuthActivity

        /*Fragment Transactions*/
        fun setSignUpFragment()

        fun setLogInFragment()

    }

    interface Controller : BaseController {
        /*UI*/
        fun signUpUi()

        fun loginUi()

        /*Firebase Auth*/
        fun firebaseSignUp(user: User)

        fun firebaseLogIn(user: User)
        fun forgotPassword(email: String)
    }

}