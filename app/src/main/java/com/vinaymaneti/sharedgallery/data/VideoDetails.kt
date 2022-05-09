package com.vinaymaneti.sharedgallery.data

import com.vinaymaneti.sharedgallery.listeners.MediaFolder

/**
 * Custom class for holding data of Video on the device external storage
 */
class VideoDetails(
    var videoName: String?,
    var videoPath: String?,
    var videoSize: String?,
    var videoUri: String? = ""
) : MediaFolder {

}