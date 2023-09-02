package com.thinkdifferent.scroller.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thinkdifferent.scroller.R
import com.thinkdifferent.scroller.api.ContentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageViewAdapter(private val contentList: InfiniteList<ContentInfo>) :
    RecyclerView.Adapter<ImageViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your ViewHolder components here (e.g., TextViews, ImageViews, etc.)
        val userImgView: ImageView = itemView.findViewById(R.id.userProfileView)
        val usrNameView: TextView = itemView.findViewById(R.id.usernameView)
        val usrDescView: TextView = itemView.findViewById(R.id.userDescriptionView)
        val usrTimeView: TextView = itemView.findViewById(R.id.userTimeView)
        val usrContentView: ImageView = itemView.findViewById(R.id.userContentView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to your ViewHolder components here
        val info = contentList[position]
        holder.usrNameView.text = info.userName
        holder.usrDescView.text = info.imageType
        holder.usrTimeView.text = info.downloadCount.toString()
        loadImage(holder.userImgView, info.userImageURL)
        loadImage(holder.usrContentView, info.imageURL)
    }

    private fun loadImage(view: ImageView, url: String) {
        val img: Bitmap? = ImageCacheManager.getBitmapFromCache(url)
        img?.let {
            view.setImageBitmap(img)
            Log.i("loadimage", "UsingCachedBitMap")
        } ?: run {
            view.setImageResource(R.drawable.loading_icon)
            // Launch a coroutine in the Main context
            MainScope().launch {
                try {
                    val bitmap = loadImageFromUrl(url)
                    view.setImageBitmap(bitmap)
                    ImageCacheManager.addBitmapToCache(url, bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle any errors here
                }
            }
        }
    }

    private suspend fun loadImageFromUrl(imageUrl: String): Bitmap = withContext(Dispatchers.IO) {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        val inputStream: InputStream = connection.inputStream
        return@withContext BitmapFactory.decodeStream(inputStream)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }
}