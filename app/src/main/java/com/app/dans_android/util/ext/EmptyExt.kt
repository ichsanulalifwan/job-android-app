package com.app.dans_android.util.ext

fun Boolean?.orEmpty(): Boolean {
    return this ?: false
}