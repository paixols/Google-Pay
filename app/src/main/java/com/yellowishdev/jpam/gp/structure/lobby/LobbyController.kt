package com.yellowishdev.jpam.gp.structure.lobby

import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.yellowishdev.jpam.gp.db.DatabaseHelper
import com.yellowishdev.jpam.gp.model.StripeTransaction
import com.yellowishdev.jpam.gp.model.User
import com.yellowishdev.jpam.gp.structure.auth.AuthActivity
import com.yellowishdev.jpam.gp.utils.StripePayOption
import com.yellowishdev.jpam.gp.utils.StripeTokens
import com.yellowishdev.jpam.gp.utils.UI
import java.lang.Exception

/**
 * [LobbyController]
 * Used for all network & database transactions within the relative fragments & activity
 * */
class LobbyController(private val mView: LobbyContract.View) : LobbyContract.Controller {

    /*Properties*/
    private var mAuth: FirebaseAuth
    private var mDatabase: DatabaseReference
    private lateinit var mUser: User
    private var mLastTransaction: StripeTransaction? = null
    private lateinit var mPaymentClient: PaymentsClient

    init {
        mView.controller = this
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    /*LifeCycle*/
    override fun create() {
        mView.getContext()?.let {
            mPaymentClient = Wallet.getPaymentsClient(it,
                    Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build())
        }
    }

    override fun start() {
        //Current User
        mAuth.currentUser?.let {
            mUser = User(it.displayName, it.email)
            mUser.id = it.uid
            mUser.email = it.email

            //Update UI
            mView.syncUserProfile(mUser)
        }
    }

    override fun resume() {
        readUserTransactions()
    }

    override fun pause() {
    }

    override fun stop() {
    }

    override fun destroy() {
    }

    /**
     * Firebase Auth
     * */
    override fun signOut() {

        val alertDialog = android.app.AlertDialog.Builder(mView.getContext())
        alertDialog.setTitle("${mUser.name}")
        alertDialog.setPositiveButton("sign out") { _, _ ->
            mAuth.signOut()
            mView.activity.finishAfterTransition()
            val intent = Intent(mView.activity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            mView.activity.startActivity(intent)
        }
        alertDialog.setNegativeButton("cancel") { _, _ ->
        }
        alertDialog.show()
    }

    override val getUser: User
        get() = mUser

    /**
     * [submitStripePaymentSource]
     * Process Stripe Payment using the [Card] object returned by the native Stripe Widget
     * */
    override fun submitStripePaymentSource(card: Card) {
        //Dev
        mView.activity.showToast("Valid card with number: ${card.number}")

        //TODO: Submit Stripe Native Payment
        mView.getContext()?.let {
            var stripe = Stripe(it, StripeTokens.publishableKey)
            stripe.createToken(card, object : TokenCallback {
                override fun onSuccess(token: Token?) {
                    //Save Token info to Firebase
                    token?.let {
                        //Payment Source Info to save in Firebase
                        val stripeTransaction = StripeTransaction()
                        stripeTransaction.paymentId = it.id
                        stripeTransaction.created = System.currentTimeMillis().toString()
                        stripeTransaction.type = it.type
                        stripeTransaction.liveMode = it.livemode

                        //Save in Firebase DB
                        DatabaseHelper.instance.saveStripeToken(mDatabase, getUser, stripeTransaction, { isSuccessful ->
                            if (isSuccessful) {
                                readUserTransactions()
                            }
                        })

                    }
                }

                override fun onError(error: Exception?) {
                    error?.let {
                        mView.showError(it.localizedMessage)
                    }
                }

            })
        }

        //Remove Payment UI
        mView.updateUI(UI.restore)
        //Restore Lobby UI
        mView.updateUI(StripePayOption.cancel)
    }

    /**
     * [submitStripePaymentSource]
     * Process Stripe when using Google Pay & retrieving information inside the
     * Activity's onActivityResult
     * */
    override fun submitStripePaymentSource(stripeTransaction: StripeTransaction) {
        DatabaseHelper.instance.saveStripeToken(mDatabase, getUser, stripeTransaction, { isSuccessful ->
            if (isSuccessful) {
                //Show last payment method added
                readUserTransactions()
                //Create Stripe Charge transaction triggering a GFC function
                DatabaseHelper.instance.submitPayment(mDatabase, getUser)
            }
        })
    }

    /**
     * [readUserTransactions]
     * Read & Display user transaction history from Firebase Database
     * */
    override fun readUserTransactions() {
        DatabaseHelper.instance.readLastPaymentMethod(mDatabase, mUser, { stripeTransaction ->
            stripeTransaction.second?.let {
                mView.showError(it)
            } ?: run {
                stripeTransaction.first?.let {
                    //Update Transactions UI
                    mLastTransaction = it
                    mView.updateLastTransaction()
                }
            }
        })
    }

    override val getLastTransaction: StripeTransaction?
        get() = mLastTransaction

    /**
     * [submitPayment]
     * Submit Payment to Firebase & Trigger Stripe Cloud Functiion
     * */
    override fun submitPayment() {
        DatabaseHelper.instance.submitPayment(mDatabase, mUser)
    }

    /**
     * Google Pay
     * */
    override fun isReadyToPay() {

        val request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .build()

        val task: Task<Boolean> = mPaymentClient.isReadyToPay(request)
        task.addOnCompleteListener(object : OnCompleteListener<Boolean> {
            override fun onComplete(p0: Task<Boolean>) {
                try {
                    val result = task.getResult(ApiException::class.java)
                    //Update Google Payment Option
                    mView.updateGooglePaymentOption(result)
                } catch (e: ApiException) {
                    //Error
                    mView.showError(e.localizedMessage)
                }
            }

        })
    }

    //Create Google Payment + Stripe Gateway Payment Request
    override fun createPaymentDataRequest(): PaymentDataRequest {
        val request = PaymentDataRequest.newBuilder()
                .setTransactionInfo(
                        //Transaction information
                        TransactionInfo.newBuilder()
                                .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                .setTotalPrice("10.00")
                                .setCurrencyCode("USD")
                                .build()
                )
                //Payment methods
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                //Card Requirements
                .setCardRequirements(
                        CardRequirements.newBuilder()
                                .addAllowedCardNetwork(WalletConstants.CARD_NETWORK_AMEX)
                                .addAllowedCardNetwork(WalletConstants.CARD_NETWORK_VISA)
                                .addAllowedCardNetwork(WalletConstants.CARD_NETWORK_MASTERCARD)
                                .addAllowedCardNetwork(WalletConstants.CARD_NETWORK_DISCOVER)
                                .build()
                )
        request.setPaymentMethodTokenizationParameters(createTokenizationParameters())
        return request.build()
    }

    //Tokenize Parameters
    override fun createTokenizationParameters(): PaymentMethodTokenizationParameters {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter(StripeTokens.gatewayKey, StripeTokens.stripe)
                .addParameter(StripeTokens.stripePublishableKey, StripeTokens.publishableKey)
                .addParameter("stripe:version", "6.1.2")
                .build()
    }

    override val getPaymentClient: PaymentsClient?
        get() = mPaymentClient

}