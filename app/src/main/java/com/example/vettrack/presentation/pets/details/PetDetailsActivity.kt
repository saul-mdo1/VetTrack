package com.example.vettrack.presentation.pets.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.databinding.PetDetailsActivityLayoutBinding
import com.example.vettrack.presentation.utils.PET_ID_TAG
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PetDetailsActivity : AppCompatActivity() {

    private lateinit var layout: PetDetailsActivityLayoutBinding
    private val viewModel by viewModel<PetDetailsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("PetDetailsActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.pet_details_activity_layout)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        getPetId()
        initViews()
    }

    private fun getPetId() {
        Timber.d("PetDetailsActivity_TAG: getPetId: ")
        intent.extras?.getString(PET_ID_TAG)?.let { id ->
            viewModel.documentId = id
            viewModel.getPetById()
        }
    }

    private fun initViews() {
        Timber.d("PetDetailsActivity_TAG: initViews: ")
        layout.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}