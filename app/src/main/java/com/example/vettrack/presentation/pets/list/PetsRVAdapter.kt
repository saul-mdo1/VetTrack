package com.example.vettrack.presentation.pets.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vettrack.databinding.PetItemLayoutBinding
import com.example.vettrack.presentation.base.BaseRVAdapter
import timber.log.Timber

class PetsRVAdapter(
    private val detailsClicked: (PetItemViewModel) -> Unit,
    private val updateClicked: (PetItemViewModel) -> Unit,
    private val deleteClicked: (PetItemViewModel) -> Unit
) :
    BaseRVAdapter<PetItemViewModel, PetItemLayoutBinding, PetsRVAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val itemBinding: PetItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: PetItemViewModel) {
            itemBinding.ivDetails.setOnClickListener {
                detailsClicked(item)
            }
            itemBinding.ivUpdate.setOnClickListener {
                updateClicked(item)
            }
            itemBinding.ivDelete.setOnClickListener {
                deleteClicked(item)
            }
            Timber.d("ViewHolder_TAG: bind: ${item.name}")
        }
    }

    override fun inflateDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PetItemLayoutBinding {
        layout = PetItemLayoutBinding.inflate(inflater, container, false)
        return layout
    }

    override fun getHolder(): ItemViewHolder {
        return ItemViewHolder(layout)
    }

    override fun setHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pet = itemsList[position]
        val h = holder as ItemViewHolder
        h.itemBinding.viewModel = pet
        h.bind(pet)
        h.itemBinding.executePendingBindings()
    }
}