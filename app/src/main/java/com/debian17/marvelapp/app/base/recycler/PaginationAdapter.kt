package com.debian17.marvelapp.app.base.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.debian17.marvelapp.R

abstract class PaginationAdapter<T>(
    context: Context,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    private var paginationStatus = PaginationStatus.HIDDEN

    protected val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    protected abstract fun getViewTypeForItem(position: Int): Int
    protected abstract fun onCreateViewHolderForItem(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    protected abstract fun onBindViewHolderForItem(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1 && paginationStatus == PaginationStatus.LOADING) {
            return R.layout.item_pagination
        } else {
            return getViewTypeForItem(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.item_pagination) {
            val view = layoutInflater.inflate(R.layout.item_pagination, parent, false)
            return PaginationItemViewHolder(view)
        } else {
            return onCreateViewHolderForItem(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType != R.layout.item_pagination) {
            onBindViewHolderForItem(holder, position)
        }
    }

    override fun getItemCount(): Int {
        var count = super.getItemCount()
        if (paginationStatus == PaginationStatus.LOADING) {
            count++
        }
        return count
    }

    fun showPagination() {
        if (paginationStatus != PaginationStatus.LOADING) {
            paginationStatus = PaginationStatus.LOADING
            notifyItemInserted(super.getItemCount())
        }
    }

    fun hidePagination() {
        if (paginationStatus != PaginationStatus.HIDDEN) {
            paginationStatus = PaginationStatus.HIDDEN
            notifyItemRemoved(super.getItemCount())
        }
    }

    private class PaginationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private enum class PaginationStatus {
        LOADING,
        HIDDEN
    }

}