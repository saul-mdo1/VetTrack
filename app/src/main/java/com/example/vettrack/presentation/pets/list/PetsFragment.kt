package com.example.vettrack.presentation.pets.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.PetsFragmentLayoutBinding
import com.example.vettrack.presentation.pets.details.PetDetailsActivity
import com.example.vettrack.presentation.pets.register.RegisterPetActivity
import com.example.vettrack.presentation.utils.PET_ID_TAG
import com.example.vettrack.presentation.utils.PET_TAG
import com.example.vettrack.presentation.utils.showAlertDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PetsFragment : Fragment() {

    private lateinit var layout: PetsFragmentLayoutBinding
    private val viewModel: PetsViewModel by viewModel()
    private lateinit var petsRVAdapter: PetsRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("PetsFragment_TAG: onCreateView: ")
        layout = PetsFragmentLayoutBinding.inflate(inflater, container, false)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        initRVAdapter()
        initObservers()
        initSearchView()
        getPets()

        return layout.root
    }

    private fun getPets() {
        Timber.d("PetsFragment_TAG: getPets: ")
        Session.user?.uid?.let {
            viewModel.getPets(it)
        }
    }

    private fun initRVAdapter() {
        Timber.d("PetsFragment_TAG: initRVAdapter: ")
        petsRVAdapter = PetsRVAdapter(
            detailsClicked = { petItem ->
                val intent = Intent(requireActivity(), PetDetailsActivity::class.java)
                val bundle = Bundle().apply {
                    putString(PET_ID_TAG, petItem.id)
                }
                intent.putExtras(bundle)
                activityResult.launch(intent)
            },
            updateClicked = { petItem ->
                val intent = Intent(requireActivity(), RegisterPetActivity::class.java)
                val bundle = Bundle().apply {
                    putParcelable(PET_TAG, petItem.pet)
                }
                intent.putExtras(bundle)
                activityResult.launch(intent)
            },
            deleteClicked = { petItem ->
                showConfirmDelete(petItem)
            }
        )

        layout.rvPets.apply {
            val linearLayout =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager = linearLayout
            adapter = petsRVAdapter
        }
    }

    private fun initSearchView() {
        Timber.d("PetsFragment_TAG: initSearchView: ")
        layout.svPets.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun initObservers() {
        Timber.d("PetsFragment_TAG: initObservers: ")
        viewModel.petsList.observe(requireActivity()) { petsList ->
            val list = petsList.map {
                PetItemViewModel().apply {
                    pet = it
                }
            }
            petsRVAdapter.itemsList = list
            viewModel.petsNum.postValue(list.size.toString())
        }

        viewModel.deleted.observe(requireActivity()) { deleted ->
            if (deleted) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.txt_pet_deleted), Toast.LENGTH_SHORT
                ).show()
                getPets()
            }
        }
    }

    private fun showConfirmDelete(petItem: PetItemViewModel) {
        Timber.d("PetsFragment_TAG: showConfirmDelete: pet ${petItem.name}")
        showAlertDialog(
            context = requireContext(),
            title = getString(R.string.txt_title_delete_pet_dialog),
            message = getString(R.string.txt_message_delete_pet, petItem.name),
            hasCancelButton = true,
            icon = null,
            isCancelable = true,
            onPositiveButtonClicked = {
                petItem.id?.let {
                    viewModel.deleteVisit(it)
                }
            }
        )
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Timber.d("PetsFragment_TAG: activityResult: ")
            if (it.resultCode == Activity.RESULT_OK)
                getPets()

        }
}