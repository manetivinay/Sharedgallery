package com.vinaymaneti.sharedgallery.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vinaymaneti.sharedgallery.R
import com.vinaymaneti.sharedgallery.adapter.pictureFolderAdapter
import com.vinaymaneti.sharedgallery.data.ImageFolder
import com.vinaymaneti.sharedgallery.data.VideoFolder
import com.vinaymaneti.sharedgallery.listeners.ItemClickListener
import com.vinaymaneti.sharedgallery.listeners.MediaFolder
import com.vinaymaneti.sharedgallery.utils.MarginDecoration
import com.vinaymaneti.sharedgallery.viewholder.PicHolder
import java.util.*


class GalleryViewActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var folderRecycler: RecyclerView
    private lateinit var empty: TextView

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_view)
        supportActionBar?.hide()

        checkStorageAccessPermission();

        changeStatusBarColor()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setUpViews() {
        empty = findViewById(R.id.empty)
        folderRecycler = findViewById(R.id.folderRecycler)
        folderRecycler.addItemDecoration(MarginDecoration(this))
        folderRecycler.hasFixedSize()

        val folds: ArrayList<ImageFolder> = getPicturePaths()
        val videoFolds: ArrayList<VideoFolder> = getVideoPaths()
        val mediaFolder: MutableList<MediaFolder> = ArrayList()
        mediaFolder.addAll(folds)
        mediaFolder.addAll(videoFolds)

        if (folds.isEmpty()) {
            empty.visibility = View.VISIBLE
        } else {
            val folderAdapter: RecyclerView.Adapter<*> =
                pictureFolderAdapter(mediaFolder, this@GalleryViewActivity, this)
            folderRecycler.adapter = folderAdapter
        }
    }


    /**
     * @return
     * gets all folders with pictures on the device and loads each of them in a custom object ImageFolder
     * the returns an ArrayList of these custom objects
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getPicturePaths(): ArrayList<ImageFolder> {
        val picFolders = ArrayList<ImageFolder>()
        val picPaths = ArrayList<String>()
        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        val cursor = this.contentResolver.query(allImagesUri, projection, null, null, null)
        try {
            cursor?.moveToFirst()
            do {
                val folds = ImageFolder()
                val name =
                    cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val folder =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val dataPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                var folderPaths = dataPath.substring(0, dataPath.lastIndexOf("$folder/"))
                folderPaths = "$folderPaths$folder/"

                if (!picPaths.contains(folderPaths)) {
                    picPaths.add(folderPaths)
                    folds.path = folderPaths
                    folds.folderName = folder
                    folds.firstPic =
                        dataPath //if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemView
                    folds.addPictures()
                    picFolders.add(folds)
                } else {
                    for (i in picFolders.indices) {
                        if (picFolders[i].path.equals(folderPaths)) {
                            picFolders[i].firstPic = dataPath
                            picFolders[i].addPictures()
                        }
                    }
                }
            } while (cursor!!.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        for (i in picFolders.indices) {
            Log.d(
                "picture folders",
                picFolders[i].folderName
                    .toString() + " and path = " + picFolders[i].path + " " + picFolders[i].numberOfPics
            )
        }
        return picFolders
    }

    /**
     * @return
     * gets all folders with videos on the device and loads each of them in a custom object VideoFolder
     * the returns an ArrayList of these custom objects
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getVideoPaths(): ArrayList<VideoFolder> {
        val videoFolders = ArrayList<VideoFolder>()
        val videoPaths = ArrayList<String>()
        val allVideosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID
        )
        val cursor = contentResolver.query(allVideosUri, projection, null, null, null)
        try {
            cursor?.moveToFirst()
            do {
                val folds = VideoFolder()
                val name =
                    cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val folder =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                val dataPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                val folderPaths = dataPath.replace(name, "")

                if (!videoPaths.contains(folderPaths)) {
                    videoPaths.add(folderPaths)
                    folds.path = folderPaths
                    folds.folderName = folder
                    videoFolders.add(folds)
                } else {
                    for (i in videoFolders.indices) {
                        if (videoFolders[i].path.equals(folderPaths)) {
                            videoFolders[i].firstVideo = dataPath
                            videoFolders[i].addVideos()
                        }
                    }
                }
            } while (cursor!!.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        for (i in videoFolders.indices) {
            Log.d(
                "video folders",
                videoFolders[i].folderName
                    .toString() + " and path = " + videoFolders[i].path + " " + videoFolders[i].numberOfVideos
            )
        }
        return videoFolders
    }

    override fun onPicClicked(holder: PicHolder?, position: Int, pics: ArrayList<MediaFolder>) {
        TODO("Not yet implemented")
    }


    override fun onPicClicked(pictureFolderPath: String?, folderName: String?) {
        val move = Intent(this@GalleryViewActivity, ImageDisplay::class.java)
        move.putExtra("folderPath", pictureFolderPath)
        move.putExtra("folderName", folderName)
        move.putExtra("isMediaType", "Images")
        startActivity(move)
    }

    override fun onVideoClicked(videoFolderPath: String?, folderName: String?) {
        val move = Intent(this@GalleryViewActivity, ImageDisplay::class.java)
        move.putExtra("folderPath", videoFolderPath)
        move.putExtra("folderName", folderName)
        move.putExtra("isMediaType", "Videos")
        startActivity(move)
    }


    /**
     * Default status bar height 24dp,with code API level 24
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.black)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkStorageAccessPermission() {
        //ContextCompat use to retrieve resources. It provide uniform interface to access resources.
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to access media file in your phone")
                    .setPositiveButton("OK") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this@GalleryViewActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1
                        )
                    }
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialog, which -> dialog.dismiss() }.show()
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else {
            // Permission has already been granted
            // Do nothing. Because if permission is already granted then files will be accessed/loaded in splash_screen_activity
            setUpViews()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //load data here
                //for first time data will be loaded here
                setUpViews()
            }
//            else {
//                finish()
//            }
        }
    }

}