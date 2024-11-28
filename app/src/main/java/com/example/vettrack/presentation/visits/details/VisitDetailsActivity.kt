package com.example.vettrack.presentation.visits.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.databinding.VisitDetailsActivityLayoutBinding
import com.example.vettrack.presentation.utils.VISIT_ID_TAG
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class VisitDetailsActivity : AppCompatActivity() {

    private lateinit var layout: VisitDetailsActivityLayoutBinding
    private val viewModel: VisitDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("VisitDetailsActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.visit_details_activity_layout)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        getVisitId()
        initViews()
    }

    private fun getVisitId() {
        Timber.d("VisitDetailsActivity_TAG: getVisitId: ")
        intent.extras?.getString(VISIT_ID_TAG)?.let { id ->
            viewModel.getVisitById(id)
        }
    }

    private fun initViews() {
        Timber.d("VisitDetailsActivity_TAG: initViews: ")
        layout.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}