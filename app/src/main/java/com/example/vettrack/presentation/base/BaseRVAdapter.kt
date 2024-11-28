package com.example.vettrack.presentation.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class BaseRVAdapter<T, dataBinding : ViewDataBinding, viewHolder : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected lateinit var layout: dataBinding

    var itemsList: List<T> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Timber.d("BaseRVPaginationAdapter_TAG: onCreateViewHolder: ")
        return inflateDataBinding(LayoutInflater.from(parent.context), parent).run {
            layout = this
            getHolder()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Timber.d("BaseRVPaginationAdapter_TAG: onBindViewHolder: ")
        setHolder(holder, position)
    }

    protected abstract fun inflateDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): dataBinding

    protected abstract fun getHolder(): viewHolder

    protected abstract fun setHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemCount(): Int = itemsList.size
}