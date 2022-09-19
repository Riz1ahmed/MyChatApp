package com.learner.codereducer.utils.extentions

import android.util.Size

/**Only for Int, Float, Double, String, all type of Array*/

/***/
fun Size.toPair() = Pair(this.width, this.height)
fun String?.ifNotNullAndEmpty(block: (String) -> Unit) {
    if (!isNullOrEmpty()) block(this)
}
fun <E> ArrayList<E>.removeItemIf(conditions: (E) -> Boolean) {
    forEach { if (conditions(it)) remove(it) }
}