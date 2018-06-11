package com.yellowishdev.jpam.gp.utils


/**
 * Activity Request Codes
 * */
object RequestCodes {
    const val FIREBASE_AUTH = 100
    const val APP_LOBBY = 101
    const val PROFILE_PIC = 102
    const val GOOGLE_PAY_REQ = 103
}

/**
 * Firebase Database Nodes
 * */
object DatabaseNodes {
    const val user = "user"
    const val stripeTokens = "stripeTokens"
}

/**
 * User stored properties
 * */
object DatabaseUserNodes {
    const val email = "email"
    const val name = "name"
    const val phone = "phone"
    const val zipCode = "zipCode"
    const val countryCode = "countryCode"
}

/**
 * Google Cloud Functions Node
 * */
object GCFNodes {
    const val STRIPE_CUSTOMERS = "stripe_customers"
    const val SOURCES = "sources"
    const val TOKEN = "token"
    const val CHARGES = "charges"
    const val AMOUNT = "amount"
}

/**
 * Stripe Options
 * */
object StripePayOption {
    const val native = "native"
    const val googlePay = "gp"
    const val cancel = "cancel"
}

/**
 * Stripe Tokens
 * */
object StripeTokens {
    /*Test Stripe Account Token*/
    const val publishableKey = "****ADD YOUR STRIPE TEST PUBLISHABLE KEY***" //Test Key
    const val gatewayKey = "gateway"
    const val stripe = "stripe"
    const val stripePublishableKey= "stripe:publishableKey"
    const val paymentCard = "card"
}


/**
 * UI
 * */
object UI {
    const val restore = "restore"
}

