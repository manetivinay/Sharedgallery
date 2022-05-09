package com.vinaymaneti.sharedgallery.data

import com.vinaymaneti.sharedgallery.listeners.MediaFolder

/**
 * Custom class for holding data of images on the device external storage
 */
class PictureDetails(
    var pictureName: String?,
    var picturePath: String?,
    var pictureSize: String?,
    var imageUri: String? = ""
) : MediaFolder {

}