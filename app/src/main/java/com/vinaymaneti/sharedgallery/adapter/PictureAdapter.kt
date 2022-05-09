package com.vinaymaneti.sharedgallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vinaymaneti.sharedgallery.R
import com.vinaymaneti.sharedgallery.data.VideoDetails
import com.vinaymaneti.sharedgallery.data.PictureDetails
import com.vinaymaneti.sharedgallery.listeners.ItemClickListener
import com.vinaymaneti.sharedgallery.listeners.MediaFolder
import com.vinaymaneti.sharedgallery.viewholder.PicHolder
import java.util.*

/**
 *
 * A RecyclerView Adapter class that's populates a RecyclerView with images from
 * a folder on the device external storage
 */
class PictureAdapter(
    private val pictureList: ArrayList<MediaFolder>,
    private val pictureContext: Context,
    private val picListener: ItemClickListener
) : RecyclerView.Adapter<PicHolder?>() {
    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val inflater: LayoutInflater = LayoutInflater.from(container.context)
        val cell: View = inflater.inflate(R.layout.pic_holder_item, container, false)
        return PicHolder(cell)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        val image = pictureList[position]
        if (image is PictureDetails) {
            val pictureDetails: PictureDetails = image
            Glide.with(pictureContext)
                .load(pictureDetails.picturePath)
                .apply(RequestOptions().centerCrop())
                .into(Objects.requireNonNull<ImageView>(holder.picture))
            ViewCompat.setTransitionName(holder.picture!!, position.toString() + "_image")
            holder.mediaType!!.setImageResource(R.drawable.icon_overlay_image)
            holder.picture!!.setOnClickListener(View.OnClickListener {
                picListener.onPicClicked(
                    holder,
                    position,
                    pictureList
                )
            })
        } else if (image is VideoDetails) {
            val videoDetails: VideoDetails = image
            Glide.with(pictureContext)
                .asBitmap()
                .load(videoDetails.videoPath)
                .apply(RequestOptions().centerCrop())
                .into(holder.picture!!)
            ViewCompat.setTransitionName(holder.picture!!, position.toString() + "_video")
            holder.mediaType!!.setImageResource(R.drawable.icon_overlay_video)
            holder.picture!!.setOnClickListener(View.OnClickListener {
                picListener.onPicClicked(
                    holder,
                    position,
                    pictureList
                )
            })
        }
    }


    override fun getItemCount(): Int {
        return pictureList.size
    }
}