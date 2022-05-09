package com.vinaymaneti.sharedgallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vinaymaneti.sharedgallery.listeners.MediaFolder
import com.vinaymaneti.sharedgallery.R
import com.vinaymaneti.sharedgallery.data.VideoFolder
import com.vinaymaneti.sharedgallery.data.ImageFolder
import com.vinaymaneti.sharedgallery.listeners.ItemClickListener

/**
 *
 * An adapter for populating RecyclerView with items representing folders that contain images
 */
class pictureFolderAdapter(
    folders: List<MediaFolder>,
    folderContx: Context,
    listen: ItemClickListener
) : RecyclerView.Adapter<pictureFolderAdapter.FolderHolder?>() {
    private val folders: List<MediaFolder> = folders
    private val folderContx: Context = folderContx
    private val listenToClick: ItemClickListener = listen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val cell: View = inflater.inflate(R.layout.picture_folder_item, parent, false)
        return FolderHolder(cell)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        val folder: MediaFolder = folders[position]
        if (folder is ImageFolder) {
            val folder1: ImageFolder = folder
            Glide.with(folderContx)
                .load(folder1.firstPic)
                .apply(RequestOptions().centerCrop())
                .into(holder.folderPic)

            //setting the number of images
            val text = "" + folder1.folderName
            val folderSizeString = "" + folder1.numberOfPics + " Media"
            holder.folderSize.text = folderSizeString
            holder.folderName.text = text
            holder.folderPic.setOnClickListener {
                listenToClick.onPicClicked(
                    folder1.path,
                    folder1.folderName
                )
            }
        } else {
            val VideoFolder: VideoFolder = folder as VideoFolder
            Glide.with(folderContx)
                .load((folder as VideoFolder).firstVideo)
                .apply(RequestOptions().centerCrop())
                .into(holder.folderPic)

            //setting the number of images
            val text = "" + VideoFolder.folderName
            val folderSizeString = "" + VideoFolder.numberOfVideos + " Video"
            holder.folderSize.text = folderSizeString
            holder.folderName.text = text
            holder.folderPic.setOnClickListener {
                listenToClick.onVideoClicked(
                    VideoFolder.path,
                    VideoFolder.folderName
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    inner class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderPic: ImageView = itemView.findViewById(R.id.folderPic)
        var folderName: TextView = itemView.findViewById<TextView>(R.id.folderName)

        //set textview for foldersize
        var folderSize: TextView = itemView.findViewById<TextView>(R.id.folderSize)
        var folderCard: CardView = itemView.findViewById(R.id.folderCard)

    }

}