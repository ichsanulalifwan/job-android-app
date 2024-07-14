package com.app.dans_android.util.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.isLastVisible(shouldFindLastCompletelyVisible: Boolean = true): Boolean {
    val layoutManager = this.layoutManager as? LinearLayoutManager
    val adapter = this.adapter

    if (layoutManager == null || adapter == null) return false

    val pos = if (shouldFindLastCompletelyVisible) {
        layoutManager.findLastCompletelyVisibleItemPosition()
    } else {
        layoutManager.findFirstVisibleItemPosition()
    }
    val numItems: Int = adapter.itemCount

    return (pos != RecyclerView.NO_POSITION) && (pos >= numItems - 1)
}