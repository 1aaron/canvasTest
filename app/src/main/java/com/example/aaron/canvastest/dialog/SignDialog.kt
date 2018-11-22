package com.example.aaron.canvastest.dialog

import android.app.Activity
import android.app.AlertDialog
import com.example.aaron.canvastest.R

object SignDialog {
    fun getSignDialog(activity: Activity?) : AlertDialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        builder.setView(inflater?.inflate(R.layout.sign_dialog,null))
        .setPositiveButton("Aceptar",null)
        .setNegativeButton("Cancelar",null)
        return builder.create()
    }
}