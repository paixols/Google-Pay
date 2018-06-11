package com.yellowishdev.jpam.gp.structure.core

import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.yellowishdev.jpam.gp.BuildConfig
import com.yellowishdev.jpam.gp.R
import java.util.*


abstract class CoreFragment : Fragment(), CoreActivity.OnBackPressListener {

    /**
     * Layout
     * */
    @LayoutRes
    protected abstract fun layoutId(): Int


    /**
     *  LIFECYCLE
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater.inflate(layoutId(), container, false)?.let {
            return it
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is CoreActivity) {
            (activity as CoreActivity).addOnBackPressListener(this)
        }
    }

    override fun onDestroy() {
        if (activity is CoreActivity) {
            (activity as CoreActivity).removeOnBackPressListener(this)
        }
        super.onDestroy()
    }

    /**
     * NAVIGATION
     * */
    override fun onBackPressed(): Boolean {
        return if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
            true
        } else {
            false
        }
    }

    /*Action Bar*/
    @Nullable
    fun getActionBar(): ActionBar? {
        return if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar
        } else
            null
    }

    /*Progress Dialog*/
    fun showProgress() {
        activity?.findViewById<FrameLayout>(R.id.progress_view)?.let { it.visibility = View.VISIBLE }
    }

    fun hideProgress() {
        activity?.findViewById<FrameLayout>(R.id.progress_view)?.let { it.visibility = View.GONE }
    }

    /*Error Handling*/

    private fun getCoreActivity(): CoreActivity? {
        return if (activity is CoreActivity) {
            activity as CoreActivity?
        } else {
            if (BuildConfig.DEBUG) {
                throw IllegalStateException("Parent activity doesn't extend CoreActivity")
            } else
                null
        }
    }

    fun showError(message: String) {
        context?.let {
            AlertDialog.Builder(it).setMessage(message)
                    .setPositiveButton(android.R.string.ok, null).show()
        }
    }

    fun showToast(message: String) {
        if (getCoreActivity() != null) {
            getCoreActivity()?.runOnUiThread({ Toast.makeText(getCoreActivity(), message, Toast.LENGTH_SHORT).show() })
        }
    }

    /**
     * [onCreateAnimation]
     * Overridden method to load animations prior to presentation.
     * This helps smooth the transitions.
     * */
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        var animation: Animation? = super.onCreateAnimation(transit, enter, nextAnim)

        // HW layer support only exists on API 11+
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getCoreActivity(), nextAnim)
        }

        if (animation != null) {

            Objects.requireNonNull(view)?.setLayerType(View.LAYER_TYPE_HARDWARE, null)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {

                }

                override fun onAnimationEnd(animation: Animation) {
                    if (view != null) {
                        view!!.setLayerType(View.LAYER_TYPE_NONE, null)
                    }
                }

                override fun onAnimationRepeat(animation: Animation) {

                }

                // ...other AnimationListener methods go here...
            })
        }

        return animation
    }


}