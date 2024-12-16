package com.example.vettrack.presentation.visits.scheduleVisits.register

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.ScheduleVisitFragmentLayoutBinding
import com.example.vettrack.presentation.utils.TIME_PICKER_DIALOG_TAG
import com.example.vettrack.presentation.utils.convertDateTimeToMills
import com.example.vettrack.presentation.utils.convertMillisToDate
import com.example.vettrack.presentation.utils.getTime
import com.example.vettrack.presentation.utils.openDatePicker
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ScheduleVisitFragment : Fragment() {

    private lateinit var layout: ScheduleVisitFragmentLayoutBinding
    private val viewModel: ScheduleVisitViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("ScheduleVisitFragment_TAG: onCreateView: ")
        layout = DataBindingUtil.inflate(
            inflater,
            R.layout.schedule_visit_fragment_layout,
            container,
            false
        )
        layout.lifecycleOwner = requireActivity()
        layout.viewModel = viewModel

        Session.user?.let {
            viewModel.userId = it.uid
        }

        initViews()
        initObservers()

        return layout.root
    }

    private fun initViews() {
        Timber.d("ScheduleVisitFragment_TAG: initViews: ")
        layout.tilDate.setStartIconOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        Timber.d("ScheduleVisitFragment_TAG: showDatePicker: ")
        openDatePicker(
            dateMills = MaterialDatePicker.todayInUtcMilliseconds(),
            futureConstraints = true,
            parentFragmentManager,
            onPositiveButtonClicked = { dateLong ->
                val date = convertMillisToDate(dateLong)
                openTimePicker(date)
            }
        )
    }

    private fun openTimePicker(date: String) {
        Timber.d("ScheduleVisitFragment_TAG: openTimePicker: ")
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val formattedTime = getTime(timePicker)
            val dateTime = "$date $formattedTime"
            viewModel.date.postValue(dateTime)
        }

        timePicker.addOnNegativeButtonClickListener {
            viewModel.date.postValue(date)
        }

        timePicker.show(parentFragmentManager, TIME_PICKER_DIALOG_TAG)
    }

    private fun initObservers() {
        Timber.d("ScheduleVisitFragment_TAG: initObservers: ")
        viewModel.successOperation.observe(requireActivity()) { success ->
            if (!success) {
                displayDialog(
                    isSuccess = false,
                    message = getString(R.string.txt_error_schedule_visit)
                ) { }
                return@observe
            }

            scheduleNotification()

            displayDialog(
                message = getString(
                    R.string.txt_success_schedule,
                    viewModel.date.value
                )
            ) {
                findNavController().navigate(R.id.nav_visits)
            }
        }
    }

    private fun scheduleNotification() {
        Timber.d("ScheduleVisitFragment_TAG: scheduleNotification: ")
        viewModel.date.value?.let { date ->
            val dateMillis = convertDateTimeToMills(date, 2)
            val clinic = viewModel.clinicName.value ?: "---"
            val pet = viewModel.petName.value ?: "---"

            viewModel.setNotification(
                requireContext(),
                getString(R.string.txt_notification_title),
                getString(R.string.txt_notification_message, pet, date, clinic),
                dateMillis ?: MaterialDatePicker.todayInUtcMilliseconds()
            )
        }
    }

    private fun displayDialog(
        isSuccess: Boolean = true,
        message: String,
        onAccept: () -> Unit
    ) {
        Timber.d("RegisterVisitActivity_TAG: displayDialog: ")

        val title = if (isSuccess)
            resources.getString(R.string.txt_success)
        else
            resources.getString(R.string.txt_error_title)

        val drawableIcon = if (isSuccess)
            AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_success)
        else
            AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_error)

        drawableIcon?.setTint(resources.getColor(R.color.md_theme_primary, null))

        val dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setIcon(drawableIcon)
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.txt_accept)) { _, _ ->
                onAccept()
            }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(resources.getColor(R.color.md_theme_primary, null))

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val layoutParams = positiveButton.layoutParams as LinearLayout.LayoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            positiveButton.layoutParams = layoutParams
            positiveButton.gravity = Gravity.CENTER
        }

        dialog.show()
    }
}