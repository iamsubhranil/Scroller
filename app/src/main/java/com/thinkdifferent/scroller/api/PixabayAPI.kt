package com.thinkdifferent.scroller.api
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private val APIKEY = "39198927-c1869b3a55dfcb01de7068d6f"
private val APILINK = "https://pixabay.com/api/?"

data class ApiResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("totalHits") val totalHits: Int,
    @SerializedName("hits") val hits: List<Hit>
)

data class Hit(
    @SerializedName("id") val id: Int,
    @SerializedName("pageURL") val pageURL: String,
    @SerializedName("type") val type: String,
    @SerializedName("tags") val tags: String,
    @SerializedName("previewURL") val previewURL: String,
    @SerializedName("previewWidth") val previewWidth: Int,
    @SerializedName("previewHeight") val previewHeight: Int,
    @SerializedName("webformatURL") val webformatURL: String,
    @SerializedName("webformatWidth") val webformatWidth: Int,
    @SerializedName("webformatHeight") val webformatHeight: Int,
    @SerializedName("largeImageURL") val largeImageURL: String,
    @SerializedName("fullHDURL") val fullHDURL: String,
    @SerializedName("imageURL") val imageURL: String,
    @SerializedName("imageWidth") val imageWidth: Int,
    @SerializedName("imageHeight") val imageHeight: Int,
    @SerializedName("imageSize") val imageSize: Int,
    @SerializedName("views") val views: Int,
    @SerializedName("downloads") val downloads: Int,
    @SerializedName("likes") val likes: Int,
    @SerializedName("comments") val comments: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user") val user: String,
    @SerializedName("userImageURL") val userImageURL: String
)

private fun sendGetRequest(page: Int, itemsPerPage: Int, params: Map<String, String>? = null): String {
    var reqParam = "key=$APIKEY&page=$page&per_page=$itemsPerPage"

    params?.let {
        for (k in params.keys) {
            reqParam += "&" + k + "=" + URLEncoder.encode(params[k], "UTF-8")
        }
    }

    val mURL = URL(APILINK + reqParam)

    with(mURL.openConnection() as HttpURLConnection) {
        // optional default is GET
        requestMethod = "GET"

        println("URL : $url")
        println("Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
            println("Response : $response")
            return response.toString()
        }
    }
}


class PixabayAPI {
    companion object {
        fun getImages(page: Int, itemsPerPage: Int): List<ContentInfo> {
            val result = sendGetRequest(page + 1, itemsPerPage)
            // Parse the JSON response
            val gson = Gson()
            val apiResponse = gson.fromJson(result, ApiResponse::class.java)
            // val total = apiResponse.total
            // val totalHits = apiResponse.totalHits
            return apiResponse.hits.map { ContentInfo(it.user, it.userImageURL, it.type, it.largeImageURL, it.downloads) }
        }
    }
}
