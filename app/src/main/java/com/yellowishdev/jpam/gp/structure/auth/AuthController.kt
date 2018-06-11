package com.yellowishdev.jpam.gp.structure.auth

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yellowishdev.jpam.gp.db.DatabaseHelper
import com.yellowishdev.jpam.gp.model.User
import com.yellowishdev.jpam.gp.structure.lobby.LobbyActivity
import com.yellowishdev.jpam.gp.utils.RequestCodes

class AuthController(val mView: AuthContract.View) : AuthContract.Controller {

    /*Properties*/
    private var mAuth: FirebaseAuth
    private var mDatabase: DatabaseReference

    init {
        mView.controller = this
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    /**
     * LIFECYCLE
     * */
    override fun create() {
    }

    override fun start() {
        //Check Firebase Auth
        mAuth.currentUser?.let {
            //Transition to App
            startAppLobby()
        }
    }

    override fun resume() {

    }

    override fun pause() {
    }

    override fun stop() {
    }

    override fun destroy() {
    }

    /**
     * Fragment Transactions
     * */
    override fun signUpUi() {
        mView.setSignUpFragment()
    }

    override fun loginUi() {
        mView.setLogInFragment()
    }

    /**
     * Firebase Auth
     * */
    override fun firebaseSignUp(user: User) {
        mView.showProgress()
        //Sign Up
        user.password?.let { pwd ->
            user.email?.let { email ->
                mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { p0 ->
                    if (p0.isSuccessful) {

                        //Save user in Firebase Database
                        user.id = p0.result.user.uid
                        DatabaseHelper.instance.createFirebaseUser(mDatabase, user)

                        //Update user display name in Firebase Auth
                        val profileUpdate = UserProfileChangeRequest.Builder()
                        profileUpdate.setDisplayName(user.name)
                        p0.result.user?.updateProfile(profileUpdate.build())

                        //Delay to make sure the user display name is updated
                        android.os.Handler().postDelayed({
                            //Transition to App
                            mView.hideProgress()
                            startAppLobby()
                        }, 3000)

                    } else {
                        mView.hideProgress()
                        p0.exception?.localizedMessage?.let {
                            mView.showError(it)
                        }
                    }
                }
            }
        }
    }

    override fun firebaseLogIn(user: User) {
        mView.showProgress()
        user.email?.let { email ->
            user.password?.let { pwd ->
                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { p0 ->
                    if (p0.isSuccessful) {
                        //Transition to App
                        startAppLobby()
                    } else {
                        mView.hideProgress()
                        p0.exception?.localizedMessage?.let {
                            mView.showError(it)
                        }
                    }
                }
            }
        }
    }

    override fun forgotPassword(email: String) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                mView.showError("Check your email!")
            } else {
                p0.exception?.localizedMessage?.let {
                    mView.showError(it)
                }
            }
        }
    }

    /**
     * APP
     * */
    private fun startAppLobby() {
        val intent = Intent(mView.getContext(), LobbyActivity::class.java)
        mView.activity.startActivityForResult(intent, RequestCodes.APP_LOBBY)
    }
}