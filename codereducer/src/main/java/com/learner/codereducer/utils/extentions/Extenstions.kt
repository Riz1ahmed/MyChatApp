package com.learner.codereducer.utils.extentions

import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Size
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import java.util.*

/** @author [Riz1Ahmed](https://fb.com/Riz1Ahmed)
 *
 * Date: 12/12/2021*/

//fun ImageView.setBitmap(assetPath: String) = Glide.with(this).load(assetPath).into(this)

fun Drawable.changeColor(color: Int) {
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        this.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    else this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)*/
    //this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    //LogD("Here color=$color")
    this.colorFilter =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
}

fun logD(msg: String, tag: String = "xyz") = Log.d(tag, msg)
