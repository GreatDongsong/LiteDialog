package com.libratone.v3.widget.dialog

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout

/**
 * create by Dawson.Gao
 * on 2018/9/3
 *
 * Initialize the builder with a valid context in order to inflate the dialog
 */
class SmartDialogBuilder constructor(var context: Context?) {

    private val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    )

    internal var smartDialogHolder: SmartDialogHolder? = null
    internal var onClickListener: OnClickListener? = null
    internal var onDismissListener: OnDismissListener? = null
    internal var onCancelListener: OnCancelListener? = null
    internal var onBackPressListener: OnBackPressListener? = null

    internal var isCancelable = true
    internal var inAnimation: Animation? = null
    internal var outAnimation: Animation? = null
    internal var expanded: Boolean = false
    private var defaultContentHeight: Int = 0

    val contentParams: FrameLayout.LayoutParams
        get() {
            if (expanded) {
                params.height = getDefaultContentHeight()
            }
            return params
        }

    val outLayoutParams: FrameLayout.LayoutParams
        get() {
            return FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

    init {
        if (context == null) {
            throw NullPointerException("Context may not be null")
        }
    }

    /**
     * Define if the dialog is cancelable and should be closed when back pressed or click outside is pressed
     */
    fun setCancelable(isCancelable: Boolean): SmartDialogBuilder {
        this.isCancelable = isCancelable
        return this
    }

    /**
     * Set the content of the dialog by passing one of the provided Holders
     */
    fun setContentHolder(smartDialogHolder: SmartDialogHolder): SmartDialogBuilder {
        this.smartDialogHolder = smartDialogHolder
        return this
    }

    /**
     * Set a global click listener to you dialog in order to handle all the possible click events. You can then
     * identify the view by using its id and handle the correct behaviour
     */
    fun setOnClickListener(listener: OnClickListener): SmartDialogBuilder {
        this.onClickListener = listener
        return this
    }

    fun setOnDismissListener(listener: OnDismissListener): SmartDialogBuilder {
        this.onDismissListener = listener
        return this
    }

    fun setOnCancelListener(listener: OnCancelListener): SmartDialogBuilder {
        this.onCancelListener = listener
        return this
    }

    fun setOnBackPressListener(listener: OnBackPressListener): SmartDialogBuilder {
        this.onBackPressListener = listener
        return this
    }

    /**
     * Create the dialog using this builder
     */
    fun create(): SmartDialog {
        return SmartDialog(this)
    }

    fun getDefaultContentHeight(): Int {
        val activity = context as Activity
        val display = activity.windowManager.defaultDisplay
        val displayHeight = display.height - SmartDialogUtils.getStatusBarHeight(activity)
        if (defaultContentHeight == 0) {
            defaultContentHeight = displayHeight * 2 / 5
        }
        return defaultContentHeight
    }
}