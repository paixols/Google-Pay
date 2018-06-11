package com.yellowishdev.jpam.gp.structure.lobby.stripe

import android.util.Patterns
import android.view.View
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import com.yellowishdev.jpam.gp.structure.lobby.LobbyActivity
import kotlinx.android.synthetic.main.fragment_stripe_native.*
import java.util.regex.Pattern


/**
 * [AddPaymentSourceFragment]
 * Add Payment source through Stripe Native Widget interface
 * */
class AddPaymentSourceFragment : CoreFragment(), View.OnClickListener {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_stripe_native

    /*Singleton*/
    object Holder {
        val instance = AddPaymentSourceFragment()
    }

    companion object {
        val INSTANCE: AddPaymentSourceFragment by lazy { Holder.instance }
    }

    /*LifeCycle*/
    override fun onStart() {
        super.onStart()
        fsn_submit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
        //Collect Card info
            fsn_submit -> {
                validateCardInfo()
            }
        }
    }

    /**
     * [validateCardInfo]
     * Validate Card & User Information to submit payment
     * */
    private fun validateCardInfo() {
        //Phone Number
        if (fsn_input_phone_number.text.isEmpty() || !Patterns.PHONE.matcher(fsn_input_phone_number.text).matches()) {
            showToast("Invalid phone number")
            return
        }
        //Zip Code
        if (fsn_input_zipcode.text.isEmpty() || !availableZipCodesUS(fsn_input_zipcode.text.toString())) {
            showToast("Invalid zip code")
            return
        }

        //Complete Card info
        fsn_card_input_widget.card?.let {

            //Name
            val act = activity as LobbyActivity
            it.name = act.controller.getUser.name
            //Zip Code
            it.addressZip = fsn_input_zipcode.text.toString().trim()
            //Phone Number
            act.controller.getUser.countryCode = fsn_country_picker.selectedCountryCodeWithPlus.trim()
            act.controller.getUser.phone = fsn_input_phone_number.text.toString()
            act.controller.getUser.zipCode = fsn_input_zipcode.text.toString().trim()

            //Submit payment
            act.controller.submitStripePaymentSource(it)

            //UI
            clearUI()

        } ?: run {
            //Error
            showToast("Invalid card")
            return
        }
    }


    /**
     * [availableZipCodesUS]
     * Validate US Zip Code
     * */
    private fun availableZipCodesUS(isValidZipCode: String): Boolean {
        val regex = "^[0-9]{5}(?:-[0-9]{4})?$" //Valid US Zip Codes
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(isValidZipCode)
        return matcher.matches()
    }

    /**
     * [clearUI]
     * Remove UI values after successful payment
     * */
    private fun clearUI() {
        fsn_input_phone_number.text.clear()
        fsn_input_zipcode.text.clear()
        fsn_card_input_widget.clear()
    }

}