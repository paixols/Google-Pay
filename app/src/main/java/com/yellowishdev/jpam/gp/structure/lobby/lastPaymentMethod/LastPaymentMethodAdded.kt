package com.yellowishdev.jpam.gp.structure.lobby.lastPaymentMethod

import android.view.View
import com.yellowishdev.jpam.gp.R
import com.yellowishdev.jpam.gp.structure.core.CoreFragment
import com.yellowishdev.jpam.gp.structure.lobby.LobbyActivity
import kotlinx.android.synthetic.main.fragment_last_transaction.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * [LastPaymentMethodAdded]
 * Holds the last Stripe Transaction Information
 * */
class LastPaymentMethodAdded : CoreFragment(), View.OnClickListener {

    /*Layout*/
    override fun layoutId(): Int = R.layout.fragment_last_transaction

    /*Singleton*/
    object Holder {
        val instance = LastPaymentMethodAdded()
    }

    companion object {
        val INSTANCE: LastPaymentMethodAdded by lazy { Holder.instance }
    }

    /*LifeCycle*/
    override fun onStart() {
        super.onStart()
        updateUI()
        flt_pay_button.setOnClickListener(this)
    }

    /*Update Last transaction Info*/
    private fun updateUI() {
        val act = activity as LobbyActivity
        act.controller.getLastTransaction?.let {

            flt_transaction_card_view.visibility = View.VISIBLE
            flt_no_transactions.visibility = View.GONE

            flt_transaction_type.text = it.type.toString()
            it.created?.let {
                formatTransactionDate(it)?.let {
                    flt_transaction_date.text = it
                }
            }

        } ?: run {

            flt_transaction_card_view.visibility = View.GONE
            flt_no_transactions.visibility = View.VISIBLE

        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
        //Submit payment
            flt_pay_button -> {
                var act = activity as LobbyActivity
                act.controller.submitPayment()
            }
        }
    }

    /**
     * [formatTransactionDate]
     * Format transaction date to readable calendar date
     * */
    private fun formatTransactionDate(date: String): String? {
        val simpleDateFormatter = SimpleDateFormat(getString(R.string.dateFormat))
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.toLong()
        return simpleDateFormatter.format(calendar.time)
    }

}