package com.example.vettrack.presentation.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.vettrack.R
import com.example.vettrack.databinding.SignupFragmentLayoutBinding
import com.example.vettrack.presentation.utils.wait
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignUpDialogFragment : DialogFragment() {
    private lateinit var layout: SignupFragmentLayoutBinding
    private val viewModel: SignUpViewModel by viewModel()
    private var listener: SignUpListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("SignUpDialog_TAG: onCreateView: ")
        layout =
            DataBindingUtil.inflate(inflater, R.layout.signup_fragment_layout, container, false)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        configureDesign()
        initObservers()
        initViews()

        return layout.root
    }

    //region CONFIGURE DIALOG DESIGN
    override fun onStart() {
        Timber.d("SignUpDialogFragment_TAG: onStart: ")
        super.onStart()
        val dialog = dialog ?: return
        val layoutParams = dialog.window?.attributes
        layoutParams?.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }

    private fun configureDesign() {
        Timber.d("SignUpDialogFragment_TAG: configureDesign: ")
        dialog?.window?.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.dialog_background,
                null
            )
        )
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }
    //endregion

    private fun initObservers() {
        Timber.d("SignUpDialog_TAG: initObservers: ")
        viewModel.successRegister.observe(this) { success ->
            if (success) {
                wait(3) {
                    dismiss()
                }
            }
        }
    }

    private fun initViews() {
        Timber.d("SignUpDialogFragment_TAG: initViews: ")
        layout.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    fun setListener(listener: SignUpListener) {
        Timber.d("SignUpDialogFragment_TAG: setListener: ")
        this.listener = listener
    }
}