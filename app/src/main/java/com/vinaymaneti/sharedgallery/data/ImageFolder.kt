package com.vinaymaneti.sharedgallery.data

import com.vinaymaneti.sharedgallery.listeners.MediaFolder

/**
 *
 * Custom Class that holds information of a folder containing images
 * on the device external storage, used to populate our RecyclerView of
 * picture folders
 */
class ImageFolder : MediaFolder {
    var path: String? = null
    var folderName: String? = null
    var numberOfPics = 0
    var firstPic: String? = null

    constructor() {}
    constructor(path: String?, folderName: String?) {
        this.path = path
        this.folderName = folderName
    }

    fun addPictures() {
        numberOfPics++
    }
}