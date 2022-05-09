package com.vinaymaneti.sharedgallery.ui

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Fade
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.vinaymaneti.sharedgallery.R
import com.vinaymaneti.sharedgallery.adapter.PictureAdapter
import com.vinaymaneti.sharedgallery.data.PictureDetails
import com.vinaymaneti.sharedgallery.data.VideoDetails
import com.vinaymaneti.sharedgallery.listeners.ItemClickListener
import com.vinaymaneti.sharedgallery.listeners.MediaFolder
import com.vinaymaneti.sharedgallery.utils.MarginDecoration
import com.vinaymaneti.sharedgallery.viewholder.PicHolder
import java.util.*

class ImageDisplay : AppCompatActivity(), ItemClickListener {
    private lateinit var imageRecycler: RecyclerView
    private lateinit var allPictures: ArrayList<PictureDetails>
    private lateinit var allVideos: ArrayList<VideoDetails>
    private lateinit var allMediaFolder: ArrayList<MediaFolder>
    private lateinit var load: ProgressBar
    private lateinit var folderPath: String
    private lateinit var isMediaType: String
    private lateinit var folderName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)

        supportActionBar?.hide()

        folderName = findViewById(R.id.foldername)
        imageRecycler = findViewById(R.id.recycler)

        folderName.text = intent.getStringExtra("folderName")
        folderPath = intent.getStringExtra("folderPath").toString()
        isMediaType = intent.getStringExtra("isMediaType").toString()

        allPictures = ArrayList<PictureDetails>()
        allVideos = ArrayList<VideoDetails>()
        allMediaFolder = ArrayList<MediaFolder>()

        imageRecycler.addItemDecoration(MarginDecoration(this))
        imageRecycler.hasFixedSize()
        load = findViewById(R.id.loader)

        if (isMediaType == "Images" && allPictures.isEmpty()) {
            load.visibility = View.VISIBLE
            allPictures = getAllImagesByFolder(folderPath)
            allMediaFolder.addAll(allPictures)
            imageRecycler.adapter = PictureAdapter(allMediaFolder, this@ImageDisplay, this)
            load.visibility = View.GONE
        } else if (isMediaType == "Videos" && allVideos.isEmpty()) {
            load.visibility = View.VISIBLE
            allVideos = getAllVideosByFolder(folderPath)
            allMediaFolder.addAll(allVideos)
            imageRecycler.adapter = PictureAdapter(allMediaFolder, this@ImageDisplay, this)
            load.visibility = View.GONE
        }
    }

    /**
     *
     * @param holder The ViewHolder for the clicked picture
     * @param position The position in the grid of the picture that was clicked
     * @param pics An ArrayList of all the items in the Adapter
     */
    override fun onPicClicked(holder: PicHolder?, position: Int, pics: ArrayList<MediaFolder>) {
        val browser = PictureBrowserFragment(pics, position)

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browser.enterTransition = Fade()
            browser.exitTransition = Fade()
        }
        supportFragmentManager
            .beginTransaction()
            .addSharedElement(holder!!.picture!!, position.toString() + "picture")
            .add(R.id.displayContainer, browser)
            .addToBackStack(null)
            .commit()
    }


    override fun onPicClicked(pictureFolderPath: String?, folderName: String?) {
        Log.d("ImageDisplay ::", "onPicClicked")
        // not required in this class, I need to follow Interface segregation principle
    }

    override fun onVideoClicked(videoFolderPath: String?, folderName: String?) {
        Log.d("ImageDisplay ::", "onVideoClicked")
        // not required in this class, I need to follow Interface segregation principle
    }

    /**
     * This Method gets all the images in the folder paths passed as a String to the method and returns
     * and ArrayList of PictureDetails a custom object that holds data of a given image
     * @param path a String corresponding to a folder path on the device external storage
     */
    private fun getAllImagesByFolder(path: String?): ArrayList<PictureDetails> {
        var images: ArrayList<PictureDetails> = ArrayList<PictureDetails>()
        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val cursor = this@ImageDisplay.contentResolver.query(
            allImagesUri, projection, MediaStore.Images.Media.DATA + " like ? ", arrayOf(
                "%$path%"
            ), null
        )
        try {
            cursor!!.moveToFirst()
            do {
                val pictureDetails = PictureDetails(
                    pictureName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                    picturePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
                    pictureSize = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                )
//                pictureDetails.pictureName =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
//                pictureDetails.picturePath =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
//                pictureDetails.pictureSize =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                images.add(pictureDetails)
            } while (cursor.moveToNext())
            cursor.close()
            val reverseSelection: ArrayList<PictureDetails> = ArrayList<PictureDetails>()
            for (i in images.size - 1 downTo -1 + 1) {
                reverseSelection.add(images[i])
            }
            images = reverseSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }

    /**
     * This Method gets all the Videos in the folder paths passed as a String to the method and returns
     * and ArrayList of VideoDetails a custom object that holds data of a given image
     * @param path a String corresponding to a folder path on the device external storage
     */
    private fun getAllVideosByFolder(path: String?): ArrayList<VideoDetails> {
        var videos: ArrayList<VideoDetails> = ArrayList<VideoDetails>()
        val allVideosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE
        )
        val cursor = this@ImageDisplay.contentResolver.query(
            allVideosUri, projection, MediaStore.Video.Media.DATA + " like ? ", arrayOf(
                "%$path%"
            ), null
        )
        try {
            cursor!!.moveToFirst()
            do {
                val videoDetails = VideoDetails(
                    videoName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)),
                    videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                    videoSize = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                )
//                videoDetails.videoName =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
//                videoDetails.videoPath =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
//                videoDetails.videoSize =
//                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                videos.add(videoDetails)
            } while (cursor.moveToNext())
            cursor.close()
            val reverseSelection: ArrayList<VideoDetails> = ArrayList<VideoDetails>()
            for (i in videos.size - 1 downTo -1 + 1) {
                reverseSelection.add(videos[i])
            }
            videos = reverseSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return videos
    }
}