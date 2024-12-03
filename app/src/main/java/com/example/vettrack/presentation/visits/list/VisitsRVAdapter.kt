package com.example.vettrack.presentation.visits.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vettrack.databinding.VisitItemLayoutBinding
import com.example.vettrack.presentation.base.BaseRVAdapter
import timber.log.Timber

class VisitsRVAdapter(
    private val detailsClicked: (VisitItemViewModel) -> Unit,
    private val updateClicked: (VisitItemViewModel) -> Unit,
    private val deleteClicked: (VisitItemViewModel) -> Unit,
    private val completeVisitClicked: (VisitItemViewModel) -> Unit
) :
    BaseRVAdapter<VisitItemViewModel, VisitItemLayoutBinding, VisitsRVAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val itemBinding: VisitItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: VisitItemViewModel) {
            itemBinding.ivCompleteVisit.setOnClickListener {
                completeVisitClicked(item)
            }
            itemBinding.ivDetails.setOnClickListener {
                detailsClicked(item)
            }
            itemBinding.ivUpdate.setOnClickListener {
                updateClicked(item)
            }
            itemBinding.ivDelete.setOnClickListener {
                deleteClicked(item)
            }
            Timber.d("ViewHolder_TAG: bind: ${item.date}")
        }
    }

    override fun inflateDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VisitItemLayoutBinding {
        layout = VisitItemLayoutBinding.inflate(inflater, container, false)
        return layout
    }

    override fun getHolder(): ItemViewHolder {
        return ItemViewHolder(layout)
    }

    override fun setHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val visit = itemsList[position]
        val h = holder as ItemViewHolder
        h.itemBinding.viewModel = visit
        h.bind(visit)
        h.itemBinding.executePendingBindings()
    }
}