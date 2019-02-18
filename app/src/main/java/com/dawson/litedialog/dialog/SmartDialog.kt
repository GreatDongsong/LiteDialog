package com.libratone.v3.widget.dialog

import android.app.Activity
import android.content.Context
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import com.dawson.litedialog.R

/**
 * create by Dawson.Gao
 * on 2018/9/3
 */
class SmartDialog internal constructor(builder: SmartDialogBuilder) {

    private var rootView: ViewGroup? = null
    private var contentContainer: ViewGroup? = null
    private var isCancelable: Boolean = false
    private var isDismissing: Boolean = false
    private var onClickListener: OnClickListener? = null
    private var onDismissListener: OnDismissListener? = null
    private var onCancelListener: OnCancelListener? = null
    private var onBackPressListener: OnBackPressListener? = null
    private var smartDialogHolder: SmartDialogHolder? = null
    private var decorView: ViewGroup? = null
    private var outAnim: Animation? = null
    private var inAnim: Animation? = null
    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private val onCancelableTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            onCancelListener?.onCancel(this@SmartDialog)
            dismiss()
        }
        false
    }

    init {
        val layoutInflater = LayoutInflater.from(builder.context)

        val activity = builder.context as Activity

        smartDialogHolder = builder.smartDialogHolder as SmartDialogHolder
        onClickListener = builder.onClickListener
        onDismissListener = builder.onDismissListener
        onCancelListener = builder.onCancelListener
        onBackPressListener = builder.onBackPressListener
        isCancelable = builder.isCancelable

        /**
         * Avoid getting directly from the decor view because by doing that we are overlapping the black soft key on
         * nexus device. I think it should be tested on different devices but in my opinion is the way to go.
         * @link http://stackoverflow.com/questions/4486034/get-root-view-from-current-activity
         */
        decorView = activity.window.decorView?.findViewById<View>(android.R.id.content) as ViewGroup
        rootView = layoutInflater.inflate(R.layout.smart_dialog_base_container, decorView, false) as ViewGroup
        rootView?.layoutParams = builder.outLayoutParams

        contentContainer = rootView?.findViewById<View>(R.id.content_container) as ViewGroup
        contentContainer?.layoutParams = builder.contentParams

        outAnim = builder.outAnimation
        inAnim = builder.inAnimation

        initContentView(layoutInflater)

        initCancelable()

        inAnim = AlphaAnimation(0f, 1.0f)
        inAnim?.duration = 300
        inAnim?.fillAfter = true

        outAnim = AlphaAnimation(1.0f, 0f)
        outAnim?.duration = 300
        outAnim?.fillAfter = true
    }

    /**
     * It basically check if the rootView contains the dialog plus view.
     *
     * @return true if it contains
     */
    val isShowing: Boolean
        get() {
            val view = decorView?.findViewById<View>(R.id.outmost_container)
            return view != null
        }

    /**
     * It adds the dialog view into rootView which is decorView of activity
     */
    fun show() {
        onAttached(rootView)
    }

    /**
     * It is called when to dismiss the dialog, either by calling dismiss() method or with cancellable
     */
    fun dismiss() {
        if (isDismissing) {
            return
        }

        outAnim?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                decorView?.post {
                    decorView?.removeView(rootView)
                    isDismissing = false
                    onDismissListener?.onDismiss(this@SmartDialog)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        if (outAnim != null) {
            contentContainer?.startAnimation(outAnim)
        }
        isDismissing = true
    }

    /**
     * It is called in order to create content
     */
    private fun initContentView(inflater: LayoutInflater) {
        val contentView = createView(inflater)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        contentView?.layoutParams = params
        contentContainer?.addView(contentView)
    }

    /**
     * It is called to set whether the dialog is cancellable by pressing back button or
     * touching the black overlay
     */
    private fun initCancelable() {
        if (!isCancelable) {
            return
        }
        val view = rootView?.findViewById<View>(R.id.outmost_container)
        view?.setOnTouchListener(onCancelableTouchListener)
    }

    /**
     * it is called when the content view is created
     *
     * @param inflater used to inflate the content of the dialog
     * @return any view which is passed
     */
    private fun createView(inflater: LayoutInflater): View? {
        val view = smartDialogHolder?.getView(inflater, rootView)

        if (smartDialogHolder is ViewHolder) {
            assignClickListenerRecursively(view)
        }
        return view
    }

    /**
     * Loop among the views in the hierarchy and assign listener to them
     */
    private fun assignClickListenerRecursively(parent: View?) {
        if (parent == null) {
            return
        }

        if (parent is ViewGroup) {
            val viewGroup = parent as ViewGroup?
            val childCount = viewGroup!!.childCount
            for (i in childCount - 1 downTo 0) {
                val child = viewGroup.getChildAt(i)
                assignClickListenerRecursively(child)
            }
        }
    }

    /**
     * It is called when the show() method is called
     *
     * @param view is the dialog plus view
     */
    private fun onAttached(view: View?) {
        if (view == null) {
            return
        }

        decorView?.addView(view)
        if (inAnim != null) {
            contentContainer?.startAnimation(inAnim)
        }

        contentContainer?.requestFocus()
        smartDialogHolder?.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            when (event.action) {
                KeyEvent.ACTION_UP -> if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressListener?.onBackPressed(this@SmartDialog)
                    if (isCancelable) {
                        onBackPressed()
                    }
                    return@OnKeyListener true
                }
                else -> {
                }
            }
            false
        })
    }

    private fun onBackPressed() {
        onCancelListener?.onCancel(this@SmartDialog)
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newDialog(context: Context): SmartDialogBuilder {
            return SmartDialogBuilder(context)
        }
    }
}
