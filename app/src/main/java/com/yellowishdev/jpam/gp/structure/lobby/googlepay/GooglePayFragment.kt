package com.yellowishdev.jpam.gp.structure.lobby.googlepay

import android.view.View
import com.google.android.gms.wallet.AutoResolveHelper
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import com.yellowishdev.jpam.gp.structure.lobby.LobbyActivity
import com.yellowishdev.jpam.gp.utils.RequestCodes
import kotlinx.android.synthetic.main.googlepay_button.*

/**
 * [GooglePayFragment]
 * Used for processing Payments using the Google Pay API
 * */
class GooglePayFragment : CoreFragment(), View.OnClickListener {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_googlepay

    /*Singleton*/
    object Holder {
        val instance = GooglePayFragment()
    }

    companion object {
        val INSTANCE: GooglePayFragment by lazy { Holder.instance }
    }


    /*LifeCycle*/
    override fun onStart() {
        super.onStart()
        //Listeners
        gpb_pay_button.setOnClickListener(this)
        //Check Google Pay availability
        val act = activity as LobbyActivity
        act.controller.isReadyToPay()
    }

    override fun onClick(p0: View?) {
        when (p0) {
        //Start Google Pay
            gpb_pay_button -> {
                val act = activity as LobbyActivity
                act.controller.createPaymentDataRequest().let { paymentDataRequest ->
                    act.controller.getPaymentClient?.let { paymentsClient ->
                        AutoResolveHelper.resolveTask(
                                paymentsClient.loadPaymentData(paymentDataRequest),
                                act, RequestCodes.GOOGLE_PAY_REQ
                        )
                    }
                }

            }
        }
    }

    /**
     * Google Pay UI
     * */
    fun updateUI(isGooglePayValid: Boolean) {
        if (isGooglePayValid) {
            gpb_pay_button.visibility = View.VISIBLE
        } else {
            gpb_pay_button.visibility = View.GONE
        }
    }

}