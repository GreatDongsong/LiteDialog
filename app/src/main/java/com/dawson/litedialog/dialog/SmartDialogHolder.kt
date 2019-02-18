package com.libratone.v3.widget.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawson.litedialog.R

/**
 * create by Dawson.Gao
 * on 2018/9/3
 */
interface SmartDialogHolder {

    val inflatedView: View?

    fun getView(inflater: LayoutInflater, parent: ViewGroup?): View?

    fun setOnKeyListener(keyListener: View.OnKeyListener)

}

class ViewHolder @JvmOverloads constructor(contentView: View? = null, viewResourceId: Int? = null) : SmartDialogHolder {
    override val inflatedView: View? = null
    private var keyListener: View.OnKeyListener? = null
    private var viewResourceId = INVALID
    private var contentView: View? = null

    init {
        if (viewResourceId != null) {
            this.viewResourceId = viewResourceId
        } else {
            this.contentView = contentView
        }
    }

    override fun getView(inflater: LayoutInflater, parent: ViewGroup?): View? {
        return if (parent == null) {
            null
        } else {
            val view = inflater.inflate(R.layout.smart_dialog_view, parent, false)
            val contentContainer = view.findViewById<View>(R.id.view_container) as ViewGroup
            contentContainer.setOnKeyListener { v, keyCode, event ->
                if (keyListener == null) {
                    throw NullPointerException("keyListener should not be null")
                }
                (keyListener?.onKey(v, keyCode, event) == true)
            }
            addContent(inflater, parent, contentContainer)
            view
        }
    }

    private fun addContent(inflater: LayoutInflater, parent: ViewGroup, container: ViewGroup) {
        if (viewResourceId != INVALID) {
            contentView = inflater.inflate(viewResourceId, parent, false)
        } else {
            val parentView = contentView?.parent as ViewGroup?
            parentView?.removeView(contentView)
        }

        if (contentView != null) {
            container.addView(contentView)
        }
    }

    override fun setOnKeyListener(keyListener: View.OnKeyListener) {
        this.keyListener = keyListener
    }

    companion object {
        private const val INVALID = 0
    }
}