package com.thinkdifferent.scroller.api

class ImageRetrieveAPI(apiKey: String) {

    private val APIKEY = apiKey
    // private var itemCount = 0

    fun retrieveImages(page: Int, itemsPerPage: Int): List<ContentInfo> {
        return PixabayAPI.getImages(page + 1, itemsPerPage)
    }

}