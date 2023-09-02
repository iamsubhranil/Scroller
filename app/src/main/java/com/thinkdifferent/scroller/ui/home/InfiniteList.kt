package com.thinkdifferent.scroller.ui.home

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfiniteList<T>(private val pageSize: Int, private val dataProvider: (Int, Int) -> List<T>) : ArrayList<T>() {
    private var currentPage = 0
    var adapter: ImageViewAdapter? = null
    private var alreadyLoading: Boolean = false

    @OptIn(DelicateCoroutinesApi::class)
    fun loadMore() {
        if(alreadyLoading)
            return
        alreadyLoading = true
        // val size = this.size;
        // Log.i("loadMore", "Loading data: prevsize: $size")
        GlobalScope.launch {
            val newData = dataProvider(currentPage, pageSize)
            // Log.i("loadMore", "Data received: $newData")
            if (newData.isNotEmpty()) {
                addAll(newData)
                currentPage++
            }
            adapter?.let {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    adapter!!.notifyItemRangeInserted((currentPage - 1) * pageSize, currentPage * pageSize)
                    /* Log.i(
                        "loadMore",
                        "Notified adapter: newsize: " + (size + newData.size).toString()
                    ) */
                }
            }
            alreadyLoading = false
        }
    }
}