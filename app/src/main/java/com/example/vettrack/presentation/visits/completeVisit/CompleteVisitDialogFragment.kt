package com.example.vettrack.presentation.visits.completeVisit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.CompleteVisitDialogLayoutBinding
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.VISIT_MODEL_TAG
import com.example.vettrack.presentation.visits.details.CompleteVisitListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CompleteVisitDialogFragment : DialogFragment() {

    private lateinit var layout: CompleteVisitDialogLayoutBinding
    private val viewModel: CompleteVisitViewModel by viewModel()
    var listener: CompleteVisitListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("CompleteVisitDialogFragment_TAG: onCreateView: ")
        layout = CompleteVisitDialogLayoutBinding.inflate(inflater, container, false)
        layout.lifecycleOwner = viewLifecycleOwner
        layout.viewModel = viewModel

        initViews()
        getVisit()

        Session.user?.uid?.let {
            viewModel.userId = it
        }

        initObservers()

        return layout.root
    }

    private fun initViews() {
        Timber.d("CompleteVisitDialogFragment_TAG: initViews: ")
        layout.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    @Suppress("DEPRECATION")
    private fun getVisit() {
        Timber.d("CompleteVisitDialogFragment_TAG: getVisit: ")
        arguments?.getParcelable<VisitModel>(VISIT_MODEL_TAG)?.let { visit ->
            viewModel.setVisitData(visit)
        }
    }

    private fun initObservers() {
        Timber.d("CompleteVisitDialogFragment_TAG: initObservers: ")
        viewModel.successOperation.observe(requireActivity()) { success ->
            if (success) {
                dismiss()
                listener?.visitCompleted(viewModel.documentId)
            }
        }
    }

    override fun onStart() {
        Timber.d("CompleteVisitDialogFragment_TAG: onStart: ")
        super.onStart()
        val dialog = dialog
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    companion object {
        fun newInstance(visit: VisitModel): CompleteVisitDialogFragment {
            Timber.d("CompleteVisitDialogFragment_TAG: newInstance: visit with date ${visit.date}")
            val bundle = Bundle().apply {
                this.putParcelable(VISIT_MODEL_TAG, visit)
            }
            val fragment = CompleteVisitDialogFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }
}