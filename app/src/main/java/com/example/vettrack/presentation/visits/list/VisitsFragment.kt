package com.example.vettrack.presentation.visits.list

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.VisitsFragmentLayoutBinding
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.COMPLETE_VISIT_FRAGMENT_TAG
import com.example.vettrack.presentation.utils.VISIT_ID_TAG
import com.example.vettrack.presentation.utils.showAlertDialog
import com.example.vettrack.presentation.visits.completeVisit.CompleteVisitDialogFragment
import com.example.vettrack.presentation.visits.completeVisit.CompleteVisitListener
import com.example.vettrack.presentation.visits.details.VisitDetailsActivity
import com.example.vettrack.presentation.visits.register.RegisterVisitActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class VisitsFragment : Fragment(), CompleteVisitListener {

    private lateinit var layout: VisitsFragmentLayoutBinding
    private val viewModel: VisitsViewModel by viewModel()
    private lateinit var visitsRVAdapter: VisitsRVAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("VisitsFragment_TAG: onCreateView: ")
        layout = DataBindingUtil.inflate(
            inflater,
            R.layout.visits_fragment_layout,
            container,
            false
        )
        layout.lifecycleOwner = requireActivity()
        layout.viewModel = viewModel

        initRecyclerView()
        initObservers()
        initSearchView()

        getVisits()

        return layout.root
    }

    private fun getVisits() {
        Timber.d("VisitsFragment_TAG: getVisits: ")
        Session.user?.uid?.let {
            viewModel.getVisits(it)
        }
    }

    private fun initRecyclerView() {
        Timber.d("VisitsFragment_TAG: initRecyclerView: ")
        visitsRVAdapter = VisitsRVAdapter(
            detailsClicked = { visitItem ->
                val intent = Intent(requireActivity(), VisitDetailsActivity::class.java)
                intent.putExtra(VISIT_ID_TAG, visitItem.id)
                startActivity(intent)
            },
            updateClicked = { visitItem ->
                val intent = Intent(requireActivity(), RegisterVisitActivity::class.java)
                intent.putExtra(VISIT_ID_TAG, visitItem.id)
                activityResult.launch(intent)
            },
            deleteClicked = { visitItem ->
                showConfirmDelete(visitItem)
            },
            completeVisitClicked = { visitItem ->
                visitItem.visit?.let { visit ->
                    val df = CompleteVisitDialogFragment.newInstance(visit)
                    df.listener = this
                    df.show(childFragmentManager, COMPLETE_VISIT_FRAGMENT_TAG)
                }
            }
        )

        layout.rvVisits.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = visitsRVAdapter
        }
    }

    private fun showConfirmDelete(visitItem: VisitItemViewModel) {
        Timber.d("VisitsFragment_TAG: showConfirmDelete: visit on ${visitItem.date} ")
        showAlertDialog(
            context = requireContext(),
            title = getString(R.string.txt_title_delete_visit_dialog),
            message = getString(R.string.txt_message_delete_visit, visitItem.date),
            hasCancelButton = true,
            isCancelable = true,
            icon = null,
            onPositiveButtonClicked = {
                visitItem.id?.let {
                    viewModel.deleteVisit(it)
                }
            }
        )
    }

    private fun initObservers() {
        Timber.d("VisitsFragment_TAG: initObservers: ")
        viewModel.visitsList.observe(requireActivity()) { visits ->
            mapList(visits)
            viewModel.loading.postValue(false)
        }

        viewModel.deleted.observe(requireActivity()) { deleted ->
            if (deleted) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.txt_visit_deleted), Toast.LENGTH_SHORT
                ).show()
                getVisits()
            }
        }
    }

    private fun initSearchView() {
        Timber.d("VisitsFragment_TAG: initSearchView: ")
        layout.svBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.searchQuery.postValue(null)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.searchQuery.postValue(query)
                }
                return true
            }
        })
    }

    private fun mapList(visits: List<VisitModel>) {
        Timber.d("VisitsFragment_TAG: mapList: ")
        val list = visits.map { v ->
            VisitItemViewModel().apply {
                visit = v
            }
        }
        viewModel.visitsNum.postValue(list.size.toString())
        visitsRVAdapter.itemsList = list
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Timber.d("VisitsFragment_TAG: activityResult: ")
            if (it.resultCode == Activity.RESULT_OK) {
                getVisits()
            }
        }

    override fun visitCompleted() {
        Timber.d("VisitsFragment_TAG: visitCompleted: ")
        viewModel.loading.postValue(true)
        getVisits()
    }
}