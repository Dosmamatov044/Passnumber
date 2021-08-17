package ru.smd.passnumber.data.core

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PaginationRecyclerView : RecyclerView {
    var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 1
    var totalItemCount = 0

    constructor(@NonNull context: Context) : super(context) {}
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        addListener()
    }

    private fun addListener() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = layoutManager as LinearLayoutManager?
                totalItemCount = adapter!!.itemCount
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && layoutManager!!.findLastCompletelyVisibleItemPosition() == totalItemCount - visibleThreshold) {
                    listener!!.loadMore()
                    loading = true
                }
            }
        })
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        addListener()
    }

    private var listener: PaginationRecyclerEvents? = null
    fun setListener(listener: PaginationRecyclerEvents?) {
        this.listener = listener
    }

    interface PaginationRecyclerEvents {
        fun loadMore()
    }
}