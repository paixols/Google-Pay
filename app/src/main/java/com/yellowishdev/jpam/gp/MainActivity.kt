package com.yellowishdev.jpam.gp

import android.app.Activity
import android.content.Intent
import android.os.Handler
import com.yellowishdev.jpam.gp.structure.auth.AuthActivity
import com.yellowishdev.jpam.gp.structure.core.CoreActivity
import com.yellowishdev.jpam.gp.utils.RequestCodes

/**
 * [MainActivity]
 * Verifies Firebase authentication & redirects user
 * */
class MainActivity : CoreActivity() {

    /*Layout*/
    override val layoutResource: Int = R.layout.activity_main

    override fun contentContainerId(): Int = 0

    /*LifeCycle*/
    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, AuthActivity::class.java)
            startActivityForResult(intent, RequestCodes.FIREBASE_AUTH)
        }, 3000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodes.FIREBASE_AUTH -> {
                    finish()
                }
            }
        }
    }

}
