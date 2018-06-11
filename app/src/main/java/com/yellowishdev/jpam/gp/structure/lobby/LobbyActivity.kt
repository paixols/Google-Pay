package com.yellowishdev.jpam.gp.structure.lobby

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.stripe.android.model.Token
import com.transitionseverywhere.*
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.db.DatabaseHelper
import com.yellowishdev.jpam.gp.model.StripeTransaction
import com.yellowishdev.jpam.gp.model.User
import com.yellowishdev.jpam.gp.structure.core.CoreActivity
import com.yellowishdev.jpam.gp.structure.lobby.empty.EmptyFragment
import com.yellowishdev.jpam.gp.structure.lobby.googlepay.GooglePayFragment
import com.yellowishdev.jpam.gp.structure.lobby.lastPaymentMethod.LastPaymentMethodAdded
import com.yellowishdev.jpam.gp.structure.lobby.profile.ProfileFragment
import com.yellowishdev.jpam.gp.structure.lobby.stripe.AddPaymentSourceFragment
import com.yellowishdev.jpam.gp.utils.RequestCodes
import com.yellowishdev.jpam.gp.utils.StripePayOption
import com.yellowishdev.jpam.gp.utils.StripeTokens
import com.yellowishdev.jpam.gp.utils.UI
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * [LobbyActivity]
 * Activity Center for all transactions with Google Pay & Stripe
 * */
class LobbyActivity : CoreActivity(), LobbyContract.View, View.OnClickListener {

    /*Layout*/
    override val layoutResource: Int = R.layout.activity_lobby

    override fun contentContainerId(): Int = R.id.al_contentContainer

    /*Controller*/
    override lateinit var controller: LobbyContract.Controller

    override fun getContext(): Context? = this

    /*Fragments*/
    private var stripeNativeFragment = AddPaymentSourceFragment.INSTANCE
    private var lastTransactionFragment = LastPaymentMethodAdded.INSTANCE
    private var googlePayFragment = GooglePayFragment.INSTANCE

    override val activity: LobbyActivity
        get() = this

    /*LifeCycle*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LobbyController(this)
        setResult(Activity.RESULT_OK)
        setSupportActionBar(toolbar)
        controller.create()
    }

    override fun onStart() {
        super.onStart()
        //Empty State Fragment
        addFragment(contentContainerId(), EmptyFragment.instance, false)
        controller.start()
        right_action.setOnClickListener(this)
        al_stripe_native.setOnClickListener(this)
        al_stripe_googlepay.setOnClickListener(this)
        left_action.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        controller.resume()
    }

    override fun onPause() {
        super.onPause()
        controller.pause()
    }

    override fun onStop() {
        super.onStop()
        controller.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.destroy()
    }

    override fun onBackPressed() {
        if (left_action.visibility == View.VISIBLE) {
            updateUI(StripePayOption.cancel)
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

        //Google Payment
            RequestCodes.GOOGLE_PAY_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            PaymentData.getFromIntent(data)?.let { paymentData ->

                                //Token
                                paymentData.paymentMethodToken?.token?.let { rawToken ->

                                    //Stripe mapping
                                    val stripeToken = Token.fromString(rawToken)
                                    val stripeTransaction = StripeTransaction()
                                    stripeTransaction.paymentId = stripeToken?.id
                                    stripeTransaction.type = StripeTokens.paymentCard
                                    stripeTransaction.liveMode = false
                                    stripeTransaction.created = System.currentTimeMillis().toString()

                                    //Submit Payment source to Firebase
                                    controller.submitStripePaymentSource(stripeTransaction)
                                }
                            }
                        }
                    }

                    AutoResolveHelper.RESULT_ERROR -> {

                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            println("Error handling Google Pay onActivityResult: $it")
                        }

                    }
                }
            }

        //Default
            else -> {
                super.onActivityResult(requestCode, resultCode, data)

            }
        }
    }

    /**
     * Profile
     * */
    override fun syncUserProfile(user: User) {
        addFragment(R.id.al_profile_container, ProfileFragment.create(controller.getUser), false)
    }

    /**
     * UI
     * */
    override fun updateUI(paymentType: String) {
        when (paymentType) {

            StripePayOption.native -> {
                //Payment buttons
                TransitionManager.beginDelayedTransition(al_buttons_container, Slide())
                al_buttons_container.visibility = View.GONE
                //Cancel
                left_action.visibility = View.VISIBLE
            }

            StripePayOption.googlePay -> {
                //Payment buttons
                TransitionManager.beginDelayedTransition(al_buttons_container, Slide())
                al_buttons_container.visibility = View.GONE
                //Cancel
                left_action.visibility = View.VISIBLE
            }

            StripePayOption.cancel -> {
                //Cancel
                if (left_action.visibility == View.VISIBLE) {
                    left_action.visibility = View.GONE
                }
                //Payment buttons
                TransitionManager.beginDelayedTransition(al_buttons_container, Slide())
                al_buttons_container.visibility = View.VISIBLE

                controller.readUserTransactions()
            }

            UI.restore -> {
                popFragment()
            }

            else -> {
                //Ignore
            }
        }
    }

    /**
     * Listeners
     * */
    override fun onClick(p0: View?) {
        when (p0) {
        /*Sign Out*/
            right_action -> {
                controller.signOut()
            }
        /*Stripe Native*/
            al_stripe_native -> {
                stripeNative()

            }
        /*Stripe GP*/
            al_stripe_googlepay -> {
                googlePay()
            }
        /*Cancel transaction*/
            left_action -> {
                removeFragment(stripeNativeFragment, true)
                updateUI(StripePayOption.cancel)
            }
        }
    }

    /**
     * Stripe Native
     * */
    override fun stripeNative() {
        replaceFragment(contentContainerId(), stripeNativeFragment, true, false)
        updateUI(StripePayOption.native)
    }

    override fun updateLastTransaction() {
        if (lastTransactionFragment.isVisible) {
            removeFragment(lastTransactionFragment, true)
        }
        replaceFragment(contentContainerId(), lastTransactionFragment, false, false)
    }

    /**
     * Google Pay
     * */
    override fun googlePay() {
        replaceFragment(contentContainerId(), googlePayFragment, false, false)
        updateUI(StripePayOption.googlePay)
    }

    override fun updateGooglePaymentOption(isVisible: Boolean) {
        googlePayFragment.updateUI(isVisible)
    }

}