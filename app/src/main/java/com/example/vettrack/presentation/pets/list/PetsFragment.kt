package com.example.vettrack.presentation.pets.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vettrack.databinding.PetsFragmentLayoutBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PetsFragment : Fragment() {

    private lateinit var layout: PetsFragmentLayoutBinding
    private val viewModel: PetsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("PetsFragment_TAG: onCreateView: ")
        layout = PetsFragmentLayoutBinding.inflate(inflater, container, false)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        return layout.root
    }
}