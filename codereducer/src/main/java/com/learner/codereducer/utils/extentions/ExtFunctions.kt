package com.learner.codereducer.utils.extentions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.webkit.URLUtil
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * This is short form of try-catch method.
 * try() = safeArea{} or the main method.
 * catch() = return value.
 * @return The Exception of catch. If return null then have ran without any error.
 */
inline fun safeArea(block: () -> Unit): Exception? {
    return try {
        block.invoke()
        null
    } catch (e: Exception) {
        e
    }
}

fun connectViewPager2WithBtmNav(
    fm: FragmentManager, lc: Lifecycle,
    vp2: ViewPager2, bnv: BottomNavigationView, frs: List<Fragment>
) {
    class Vp2Adapter : FragmentStateAdapter(fm, lc) {
        override fun getItemCount() = frs.size
        override fun createFragment(position: Int) = frs[position]
    }
    vp2.adapter = Vp2Adapter()
    bnv.setOnItemSelectedListener {
        bnv.menu.forEachIndexed { index, item ->
            if (item.itemId == it.itemId) vp2.setCurrentItem(index).also {
                return@setOnItemSelectedListener true
            }
        }
        return@setOnItemSelectedListener false
    }
}

/**
 *This method is continuously runner. For example: if you want set a value to
 * view after view initialized (otherwise get null error, Then you have to
 * need to check the view is initialized or not.
 *
 * @param conditionCode This is your condition code. If return false then call
 * "falseCode" (2nd parameter) and then call again itself, Otherwise call
 * "trueCode". So until return true It's continuously call to [falseCode] and itself.
 * @param falseCode this method continuously call until the 1st method (conditionCode)
 * return true.
 * @param trueCode when [conditionCode] method return true then call this method.
 * And mind it, after call this method this main main not run again. Actually your
 * main code here.
 * @param delay The Delay time (in millisecond) to check [conditionCode] method.
 */
fun waitTillFalse(
    conditionCode: () -> Boolean, falseCode: () -> Unit, trueCode: () -> Unit,
    delay: Long = 100L
) {
    Handler(Looper.getMainLooper()).let { handler ->
        handler.postDelayed(object : Runnable {
            override fun run() {
                handler.removeCallbacks(this)
                if (!conditionCode.invoke()) {
                    falseCode.invoke()
                    handler.postDelayed(this, delay)
                } else trueCode.invoke()
            }
        }, 0)
    }
}

fun waitFor(delayInMillis: Long, runAfterDelay: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ runAfterDelay.invoke() }, delayInMillis)
}