package com.vinaymaneti.sharedgallery.listeners

import com.vinaymaneti.sharedgallery.viewholder.PicHolder
import java.util.*

interface ItemClickListener {
    /**
     * Called when a picture is clicked
     * @param holder The ViewHolder for the clicked picture
     * @param position The position in the grid of the picture that was clicked
     */
    fun onPicClicked(holder: PicHolder?, position: Int, pics: ArrayList<MediaFolder>)
    fun onPicClicked(pictureFolderPath: String?, folderName: String?)
    fun onVideoClicked(videoFolderPath: String?, folderName: String?)
}