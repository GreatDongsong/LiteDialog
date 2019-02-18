package com.dawson.litedialog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.libratone.v3.widget.dialog.SmartDialog
import com.libratone.v3.widget.dialog.ViewHolder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null)

        var dialogBuilder = SmartDialog.newDialog(this)
        dialogBuilder.setContentHolder(ViewHolder(view))
        var window: SmartDialog?
        window = dialogBuilder.create()
        window.show()
    }
}
