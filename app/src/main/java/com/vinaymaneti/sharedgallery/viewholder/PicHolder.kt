package com.vinaymaneti.sharedgallery.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vinaymaneti.sharedgallery.R

class PicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    
    var picture: ImageView? = itemView.findViewById(R.id.image)
    var mediaType: ImageView? = itemView.findViewById(R.id.media_type)
}