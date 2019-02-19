package com.dawson.litedialog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.libratone.v3.widget.dialog.OnDismissListener
import com.libratone.v3.widget.dialog.SmartDialog
import com.libratone.v3.widget.dialog.ViewHolder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showDialog(view: View) {

        var dialogBuilder = SmartDialog.newDialog(this)
        var mAlertDialog: SmartDialog? = null
        val view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_icon_yes_no, null)

        dialogBuilder = SmartDialog.newDialog(this)
        dialogBuilder.setContentHolder(ViewHolder(view))
        dialogBuilder.setOnDismissListener(object : OnDismissListener {
            override fun onDismiss(dialog: SmartDialog) {
            }
        })
        dialogBuilder.setCancelable(true)
        mAlertDialog = dialogBuilder.create()
        mAlertDialog.show()
    }
}
