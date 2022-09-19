package com.learner.codereducer.utils

import android.os.Handler
import android.os.Looper

object SystemUtils {
    private var totalTimes: Int? = null

    /**
     * @param delay in Millisecond. The delay time between a click. If the time over
     * then, it's will close and [timeOver] block will call.
     * @param totalClicked How times clicked on back button.
     * @param timeOver If the time between two click is greater than [delay] time then
     * this block will call. Also, this is the last call call of a continuously click.
     * */
    fun countBackClick(delay: Long, totalClicked: (Int) -> Unit, timeOver: () -> Unit) {

        totalTimes = if (totalTimes != null) totalTimes!! + 1 else 1
        if (this.totalTimes != null) totalClicked(this.totalTimes!!)

        Handler(Looper.getMainLooper()).postDelayed({
            this.totalTimes = null
            timeOver()
        }, delay)
    }
}