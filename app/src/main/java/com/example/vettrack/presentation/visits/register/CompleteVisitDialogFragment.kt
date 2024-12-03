package com.example.vettrack.presentation.visits.register

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.vettrack.databinding.CompleteVisitDialogLayoutBinding
import com.example.vettrack.models.VisitModel
import com.example.vettrack.presentation.utils.VISIT_MODEL_TAG
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CompleteVisitDialogFragment : DialogFragment() {

    private lateinit var layout: CompleteVisitDialogLayoutBinding
    private val viewModel: CompleteVisitViewModel by viewModel()
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
        val visit = arguments?.getParcelable<VisitModel>(VISIT_MODEL_TAG)
        if (visit != null) {
            Toast.makeText(requireActivity(), "VISIT WITH DATE: ${visit.date}", Toast.LENGTH_SHORT)
                .show()
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