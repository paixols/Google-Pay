package com.yellowishdev.jpam.gp.structure.lobby

import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters
import com.google.android.gms.wallet.PaymentsClient
import com.stripe.android.model.Card
import com.yellowishdev.jpam.gp.model.StripeTransaction
import com.yellowishdev.jpam.gp.model.User
import com.yellowishdev.jpam.gp.structure.core.BaseController
import com.yellowishdev.jpam.gp.structure.core.BaseView

interface LobbyContract {
    interface View : BaseView<Controller> {
        val activity: LobbyActivity

        fun syncUserProfile(user: User)

        /*Stripe*/
        fun stripeNative()

        fun googlePay()

        /*UI*/
        fun updateUI(paymentType: String)

        fun updateLastTransaction()

        fun updateGooglePaymentOption(isVisible: Boolean)

    }

    interface Controller : BaseController {

        /*Firebase*/
        fun signOut()

        /*Current User*/
        val getUser: User


        /*Stipe Native*/
        fun submitStripePaymentSource(card: Card)

        fun submitStripePaymentSource(stripeTransaction: StripeTransaction)

        fun readUserTransactions()
        val getLastTransaction: StripeTransaction?

        /*Payments*/
        fun submitPayment()

        /*Google Pay*/
        fun isReadyToPay()

        fun createPaymentDataRequest(): PaymentDataRequest
        fun createTokenizationParameters(): PaymentMethodTokenizationParameters
        val getPaymentClient: PaymentsClient?

    }
}