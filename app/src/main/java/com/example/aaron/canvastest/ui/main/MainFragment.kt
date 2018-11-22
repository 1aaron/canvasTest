package com.example.aaron.canvastest.ui.main

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aaron.canvastest.R
import com.example.aaron.canvastest.customView.CanvasView
import com.example.aaron.canvastest.dialog.SignDialog
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        @JvmStatic fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        signature.setOnClickListener {
            val signDialog = SignDialog.getSignDialog(activity)
            signDialog.show()
            signDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val canvas = signDialog.findViewById<CanvasView>(R.id.canvasSignature)
                //iba save()
                canvas.getBitmap()
                imgSignatureView.setImageBitmap(canvas.getBitmap())
                signDialog.dismiss()
            }
        }
    }
}
