package com.vinaymaneti.sharedgallery.data

import com.vinaymaneti.sharedgallery.listeners.MediaFolder

/**
 *
 * Custom Class that holds information of a folder containing Videos
 * on the device external storage, used to populate our RecyclerView of
 * Video folders
 */
class VideoFolder : MediaFolder {
    var path: String? = null
    var folderName: String? = null
    var numberOfVideos = 0
    var firstVideo: String? = null

    constructor() {}
    constructor(path: String?, folderName: String?) {
        this.path = path
        this.folderName = folderName
    }

    fun addVideos() {
        numberOfVideos++
    }
}