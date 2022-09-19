package com.learner.codereducer.utils.extentions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learner.codereducer.R
import com.learner.codereducer.databinding.ToastLayoutBinding
import java.util.*

fun Context.toast(msg: String, len: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, msg, len).show()

/**Custom layout on the CodeReducer module. Please design it as ur UI*/
fun Context.customToast(msg: String, len: Int = Toast.LENGTH_SHORT) {
    val toast = Toast(this)
    toast.duration = len

    val inflater: LayoutInflater =
        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding = ToastLayoutBinding.bind(inflater.inflate(R.layout.toast_layout, null))
    toast.view = binding.root
    binding.message.text = msg
    toast.show()
}

fun Activity.startActivity(cls: Class<*>) =
    Intent(this, cls).also {
        it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(it)
    }

fun DialogFragment.show(activity: AppCompatActivity, tag: String = "tag") {
    show(activity.supportFragmentManager, tag)
}

fun Activity.connectFragmentAndNavUI(
    bottomNavView: BottomNavigationView, @IdRes fragmentView: Int
) {
    NavigationUI.setupWithNavController(
        bottomNavView, Navigation.findNavController(this, fragmentView)
    )
}



/**
 * @param pkgId Your app package ID/name
 * @param drawableName The name of resource drawable as string. ex: "ic frame" or "ic_frame".
 * Here the space( ) will replace with underscore(_) automatically.
 * @param defaultRes In case of not load [drawableName], then load this drawable.
 * This is not as string. It's pass like R.drawable.drawable_name
 */
@DrawableRes
fun Context.getDrawableResId(
    pkgId: String, drawableName: String,
    @DrawableRes defaultRes: Int = R.drawable.ic_launcher_foreground
): Int {
    //LogD("ic for: $drawableName")
    resources.getIdentifier(
        drawableName.lowercase(Locale.ROOT).replace(" ", "_"), "drawable", pkgId
    ).let { resId -> return if (resId != 0) resId else defaultRes }
}

fun Context.gtColor(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}

fun Context.getDrawableImage(@DrawableRes DrawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(this, DrawableResId)
}

fun Context.getColorFromRes(@ColorRes ResId: Int): Int {
    return ContextCompat.getColor(this, ResId)
}

fun AppCompatActivity.openActivityResult(intent: Intent, block: (Uri) -> Unit) {
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null && result.data!!.data != null) {
            block(result.data!!.data!!)
        }
    }.launch(intent)
}

val Activity?.isRunning get() = (this != null && !this.isFinishing && !this.isDestroyed)
val Context?.isActivity get() = (this != null && (this is Activity) && !this.isFinishing && !this.isDestroyed)

/**
 * Return true if this [Context] is available.
 * Availability is defined as the following:
 * + [Context] is not null
 * + [Context] is not destroyed (tested with [FragmentActivity.isDestroyed] or [Activity.isDestroyed])
 */
fun Context?.isAvailable(): Boolean {
    if (this == null) return false
    else if (this !is Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (this is FragmentActivity) return !this.isDestroyed
            else if (this is Activity) return !this.isDestroyed
        }
    }
    return true
}

