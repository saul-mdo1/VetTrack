package com.example.vettrack.presentation.pets.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.PetsFragmentLayoutBinding
import com.example.vettrack.presentation.pets.register.RegisterPetActivity
import com.example.vettrack.presentation.utils.PET_TAG
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
                Toast.makeText(
                    requireActivity(),
                    "DETAILS OF PET WITH ID: ${petItem.id}",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    requireActivity(),
                    "DELETE PET WITH ID: ${petItem.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        layout.rvPets.apply {
            val linearLayout =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager = linearLayout
            adapter = petsRVAdapter
        }
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
        }
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Timber.d("PetsFragment_TAG: activityResult: ")
            if (it.resultCode == Activity.RESULT_OK)
                getPets()

        }
}