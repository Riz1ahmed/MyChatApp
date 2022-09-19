package com.learner.codereducer.utils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

/**
 * Must be initialize before onStart(). Recommend you to get instance as a global
 */
class MyActivityResult(ca: ComponentActivity) {
    private var listener: ((ActivityResult?) -> Unit)? = null
    private val launcher =
        ca.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                listener?.let { it(result) }
            }
        }

    fun startIntent(intent: Intent, block: (ActivityResult?) -> Unit) {
        listener = block
        launcher.launch(intent)
    }
    fun getResultListener(){

    }
}