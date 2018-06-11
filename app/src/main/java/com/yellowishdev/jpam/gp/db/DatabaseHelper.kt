package com.yellowishdev.jpam.gp.db

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.yellowishdev.jpam.gp.model.StripeTransaction
import com.yellowishdev.jpam.gp.model.User
import com.yellowishdev.jpam.gp.utils.DatabaseNodes
import com.yellowishdev.jpam.gp.utils.DatabaseUserNodes
import com.yellowishdev.jpam.gp.utils.GCFNodes

class DatabaseHelper {


    /*Singleton Pattern*/
    object Holder {
        val instance = DatabaseHelper()
    }

    companion object {
        val instance: DatabaseHelper by lazy { Holder.instance }
    }

    /**
     * [createFirebaseUser]
     * Helper method to create a user in the database
     * @param databaseReference
     * @param user
     *
     * This method triggers GCF to register a user with Stripe & retrieve a token ID
     * @see createStripeCustomer in Firebase Cloud Functions
     * */
    fun createFirebaseUser(databaseReference: DatabaseReference, user: User) {
        user.id?.let {
            databaseReference.child(DatabaseNodes.user).child(it).child(DatabaseUserNodes.email).setValue(user.email)
            databaseReference.child(DatabaseNodes.user).child(it).child(DatabaseUserNodes.name).setValue(user.name)
        }
    }

    /**
     * [saveStripeToken]
     * Save Stripe Token Information in Firebase
     * */
    fun saveStripeToken(databaseReference: DatabaseReference, user: User, stripeTransaction: StripeTransaction, callback: (Boolean) -> Unit) {
        //App Info
        user.id?.let {
            databaseReference.child(DatabaseNodes.stripeTokens).child(it).setValue(stripeTransaction).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
            updatePhoneNumberAndZipCode(databaseReference, user)
        }

        //Trigger Stripe call to add payment source detailed information received from Stripe Server
        addPaymentSource(databaseReference, user, stripeTransaction)
    }

    /**
     * [addPaymentSource]
     * Add Payment source details when a payment source is added in the app through a Stripe Callback
     * This method triggers a Cloud Function
     * */
    private fun addPaymentSource(databaseReference: DatabaseReference, user: User, stripeTransaction: StripeTransaction) {
        user.id?.let {
            databaseReference.child(GCFNodes.STRIPE_CUSTOMERS).child(it).child(GCFNodes.SOURCES)
                    .push().child(GCFNodes.TOKEN).setValue(stripeTransaction.paymentId)
        }
    }

    /**
     * [updatePhoneNumberAndZipCode]
     * Update User phone number & country code
     * This happens usually after every transaction
     *
     * Only used for in app purposes -> Not required for billing
     * */
    private fun updatePhoneNumberAndZipCode(databaseReference: DatabaseReference, user: User) {
        user.id?.let { userId ->
            user.phone?.let {
                databaseReference.child(DatabaseNodes.user).child(userId).child(DatabaseUserNodes.phone).setValue(it)
            }
            user.countryCode?.let {
                databaseReference.child(DatabaseNodes.user).child(userId).child(DatabaseUserNodes.countryCode).setValue(it)
            }
            user.zipCode?.let {
                databaseReference.child(DatabaseNodes.user).child(userId).child(DatabaseUserNodes.zipCode).setValue(it)
            }
        }
    }

    /**
     * [readLastPaymentMethod]
     * Read user's last payment method
     *
     * Only used for in app purposes -> Not required for billing
     * */
    fun readLastPaymentMethod(databaseReference: DatabaseReference, user: User, callback: (Pair<StripeTransaction?, String?>) -> Unit) {
        user.id?.let {
            databaseReference.child(DatabaseNodes.stripeTokens).child(it).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    callback(Pair(null, p0.message))
                }

                override fun onDataChange(p0: DataSnapshot) {
                    callback(Pair(p0.getValue(StripeTransaction::class.java), null))

                }

            })
        }
    }

    /**
     * [submitPayment]
     * Post payment to a specific database node which will call a Cloud Function & trigger
     * the Stripe Transaction with the GCF (Google Cloud Firebase) backend.
     * @see createStripeCharge in Firebase Functions Console
     *
     * TODO: Remove hard-coded value of $10 for each transaction
     *
     * */
    fun submitPayment(databaseReference: DatabaseReference, user: User) {
        user.id?.let {
            databaseReference.child(GCFNodes.STRIPE_CUSTOMERS)
                    .child(it).child(GCFNodes.CHARGES).push().child(GCFNodes.AMOUNT).setValue(1000)
        }
    }

}