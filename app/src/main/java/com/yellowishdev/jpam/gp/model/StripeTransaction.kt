package com.yellowishdev.jpam.gp.model

/**
 * Model used to push && pull last added payment method to Firebase
 * */
class StripeTransaction {

    /**
     * [paymentId]
     * Represents the id for the Stripe Token
     * returned when adding a payment source
     * */
    var paymentId: String? = null
    /**
     * [created]
     * Date When the payment source was added
     * Format -> dd/MM/yyyy hh:mm:ss.SSS
     * */
    var created: String? = null
    /**
     * [liveMode]
     * Represents the state for the Added source
     * @see true -> Production
     * @see false -> Debug
     * */
    var liveMode: Boolean? = null
    /**
     * [type]
     * Type of payment added , could represent card, bank, etc...
     * */
    var type: String? = null

}