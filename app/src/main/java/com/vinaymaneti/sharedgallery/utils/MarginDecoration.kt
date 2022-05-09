package com.vinaymaneti.sharedgallery.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.vinaymaneti.sharedgallery.R

/**
 *
 * RecyclerView's item margin decorator
 */
class MarginDecoration(context: Context) : ItemDecoration() {
    private val margin: Int = context.resources.getDimensionPixelSize(R.dimen.item_margin)
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect[margin, margin, margin] = margin
    }

}