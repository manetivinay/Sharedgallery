package com.vinaymaneti.sharedgallery.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.vinaymaneti.sharedgallery.R
import com.vinaymaneti.sharedgallery.data.VideoDetails
import com.vinaymaneti.sharedgallery.data.PictureDetails
import com.vinaymaneti.sharedgallery.listeners.MediaFolder
import java.util.*

/**
 * this fragment handles the browsing of all images/Videos in an ArrayList of PictureDetails/VideoDetails
 * passed in the constructor the images/videos are loaded in a ViewPager
 */
class PictureBrowserFragment(private var allImages: ArrayList<MediaFolder>, imagePosition: Int) :
    Fragment() {
    private var position = imagePosition
    private lateinit var imagePager: ViewPager2
    private var pagingImages: ImagesPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.picture_browser, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * setting up the viewPager with images
         */
        imagePager = view.findViewById(R.id.imagePager)
        pagingImages = ImagesPagerAdapter(this)
        imagePager.adapter = pagingImages
        imagePager.setCurrentItem(position, false)
    }

    private inner class ImagesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            val allMedia = allImages[position]
            if (allMedia is PictureDetails) {
                return PictureFragment(allMedia.picturePath!!)
            } else if (allMedia is VideoDetails) {
                return VideoFragment(allMedia.videoPath!!)
            }
            return requireParentFragment()
        }

        override fun getItemCount(): Int {
            return allImages.size
        }
    }

}