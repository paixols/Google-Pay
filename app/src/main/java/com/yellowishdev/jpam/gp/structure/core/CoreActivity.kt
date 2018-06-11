package com.yellowishdev.jpam.gp.structure.core

import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.annotation.IdRes
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.yellowishdev.jpam.gp.R
import android.support.annotation.StringRes
import android.support.v4.content.res.ResourcesCompat
import android.support.annotation.ColorRes
import android.view.View
import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.EditText


abstract class CoreActivity : AppCompatActivity() {

    /*Properties*/
    private var mToolbar: Toolbar? = null
    private val mOnBackPressListeners = arrayListOf<OnBackPressListener>()


    /**
     * @return the layout paymentId associated to the layout used in the activity.
     */
    @get:LayoutRes
    protected abstract val layoutResource: Int

    //Return 0 for activity with no container
    @IdRes
    protected abstract fun contentContainerId(): Int

    /**
     * LIFECYCLE
     * */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(layoutResource)
        mToolbar = findViewById(R.id.toolbar)
    }

    /**
     * TOOLBAR
     * */
    fun setToolbarTitle(@StringRes title: Int) {
        setToolbarTitle(getString(title).toInt())
    }

    fun setToolbarColor(@ColorRes color: Int) {
        findViewById<Toolbar>(R.id.toolbar)?.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
    }

    fun getToolbar(): Toolbar? {
        return mToolbar
    }

    /**
     * NAVIGATION
     */

    fun showBackArrow(icon: Int) {
        mToolbar?.setNavigationIcon(icon)
        mToolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    fun showBackArrow(icon: Int, finish: Boolean) {
        if (finish) {
            mToolbar?.setNavigationIcon(icon)
            mToolbar?.setNavigationOnClickListener { finish() }
        } else {
            showBackArrow(icon)
        }
    }

    /**
     * PROGRESS DIALOG
     * */
    fun showProgress() {
        findViewById<FrameLayout>(R.id.progress_view)?.visibility = View.VISIBLE
    }

    fun hideProgress() {
        findViewById<FrameLayout>(R.id.progress_view)?.visibility = View.GONE
    }

    /**
     * FRAGMENTS
     * */

    @SuppressLint("CommitTransaction")
    fun addFragment(@IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean) {
        val name = fragment.javaClass.name
        val add = supportFragmentManager.beginTransaction().add(containerId, fragment, name)
        if (addToBackStack) {
            add.addToBackStack(name)
        }
        add.commit()
    }

    @SuppressLint("CommitTransaction")
    fun replaceFragment(@IdRes containerId: Int, fragment: CoreFragment, addToBackStack: Boolean, oppositeAnimation: Boolean) {
        val name = fragment.javaClass.name
        val replaceTransaction = supportFragmentManager.beginTransaction()
        if (oppositeAnimation) {
            replaceTransaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.slide_out_right)
        } else {
            replaceTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        replaceTransaction.replace(containerId, fragment, name)
        if (addToBackStack) {
            replaceTransaction.addToBackStack(name)
        }
        replaceTransaction.commit()
    }

    fun removeFragment(fragment: CoreFragment, oppositeAnimation: Boolean) {
        val removeTransaction = supportFragmentManager.beginTransaction()
        if (oppositeAnimation) {
            removeTransaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.slide_out_right)
        } else {
            removeTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
        removeTransaction.remove(fragment).commit()

    }

    fun popFragment() {
        val fm = supportFragmentManager
        fm.popBackStack()
    }

    fun closeFromChild() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            supportFinishAfterTransition()
        }
    }

    /**
     * NAVIGATION
     * */
    /*Navigation Interface - Back Button*/
    interface OnBackPressListener {
        fun onBackPressed(): Boolean
    }

    /*Navigation - Back Button*/
    override fun onBackPressed() {
        when {
            interruptedByListener() -> return
            supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    private fun interruptedByListener(): Boolean {
        var interrupt = false
        for (listener in mOnBackPressListeners) {
            if (listener.onBackPressed()) {
                interrupt = true
            }
        }
        return interrupt
    }

    /*Error handling*/
    fun showError(errorName: String) {
        AlertDialog.Builder(this).setMessage(errorName)
                .setPositiveButton(android.R.string.ok, null).show()
    }

    /*Info feedback*/
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun addOnBackPressListener(listener: OnBackPressListener?) {
        if (listener != null) {
            mOnBackPressListeners.add(listener)
        }
    }

    fun removeOnBackPressListener(listener: OnBackPressListener) {
        mOnBackPressListeners.remove(listener)
    }

    fun showAlertWithInput(title: String, callback: (String?) -> Unit) {
        val alertDialog = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.view_dialog_resetpassword, null)
        dialogLayout.background = getDrawable(android.R.color.holo_blue_light)
        alertDialog.setView(dialogLayout)
        val commentEditText = dialogLayout.findViewById<EditText>(R.id.vdrp_comment)

        commentEditText.minLines = 2
        commentEditText.setPadding(10, 0, 10, 0)
        alertDialog.setTitle(title)
        alertDialog.setPositiveButton("reset") { _, _ ->
            val answer = commentEditText.text.toString()
            callback(answer)
        }
        alertDialog.setNegativeButton("cancel") { _, _ ->
            callback(null)
        }
        alertDialog.show()
    }


}
