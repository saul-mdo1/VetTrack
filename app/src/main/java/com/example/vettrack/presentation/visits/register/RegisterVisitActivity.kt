package com.example.vettrack.presentation.visits.register

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.RegisterVisitActivityLayoutBinding
import com.example.vettrack.presentation.utils.DATE_PICKER_DIALOG_TAG
import com.example.vettrack.presentation.utils.TIME_PICKER_DIALOG_TAG
import com.example.vettrack.presentation.utils.VISIT_ID_TAG
import com.example.vettrack.presentation.utils.convertDateToMills
import com.example.vettrack.presentation.utils.convertMillisToDate
import com.example.vettrack.presentation.utils.getTime
import com.example.vettrack.presentation.utils.parseTimeToHourMinute
import com.example.vettrack.presentation.utils.showAlertDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RegisterVisitActivity : AppCompatActivity() {

    private lateinit var layout: RegisterVisitActivityLayoutBinding
    private val viewModel: RegisterVisitViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("RegisterVisitActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.register_visit_activity_layout)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.userId = Session.user?.uid ?: ""

        getVisitId()
    }

    private fun getVisitId() {
        Timber.d("VisitDetailsActivity_TAG: getVisitId: ")
        intent.extras?.getString(VISIT_ID_TAG)?.let { id ->
            viewModel.documentId = id
            viewModel.getVisitById()
        }
    }

    //region VIEWS
    private fun initViews() {
        Timber.d("RegisterVisitActivity_TAG: initViews: ")
        layout.toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        layout.tilDate.setStartIconOnClickListener {
            openDatePicker()
        }
    }

    private fun openDatePicker() {
        Timber.d("RegisterVisitActivity_TAG: openDatePicker: ")
        val dateTime: String? = if (viewModel.isUpdate) viewModel.date.value else null
        var dateMills: Long = MaterialDatePicker.todayInUtcMilliseconds()

        try {
            dateTime?.let {
                if (it.isNotBlank()) {
                    val dateString = it.split(" ")[0]
                    dateMills = convertDateToMills(dateString)
                }
            }
        } catch (e: Exception) {
            Timber.d("RegisterVisitActivity_TAG: openDatePicker: ERROR set date: ${e.message} ")
            dateMills = MaterialDatePicker.todayInUtcMilliseconds()
        }

        val constraints =
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .setSelection(dateMills)
            .build()

        datePicker.addOnPositiveButtonClickListener { dateLong ->
            val date = convertMillisToDate(dateLong)
            openTimePicker(date)
        }

        datePicker.show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
    }

    private fun openTimePicker(date: String) {
        Timber.d("RegisterVisitActivity_TAG: openTimePicker: ")
        val dateTimeString: String? = if (viewModel.isUpdate) viewModel.date.value else null
        var hour = 12
        var minute = 0

        try {
            dateTimeString?.let {
                if (it.isNotBlank()) {
                    val timeString = it.split(" ")[1] + " " + it.split(" ")[2]
                    val (h, m) = parseTimeToHourMinute(timeString)
                    hour = h
                    minute = m
                }
            }
        } catch (e: Exception) {
            Timber.d("RegisterVisitActivity_TAG: openTimePicker: ERROR set time: ${e.message} ")
            hour = 12
            minute = 0
        }

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(minute)
            .setInputMode(INPUT_MODE_KEYBOARD)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val formattedTime = getTime(timePicker)
            val dateTime = "$date $formattedTime"
            viewModel.date.postValue(dateTime)
        }

        timePicker.addOnNegativeButtonClickListener {
            viewModel.date.postValue(date)
        }

        timePicker.show(supportFragmentManager, TIME_PICKER_DIALOG_TAG)
    }
    //endregion

    private fun initObservers() {
        Timber.d("RegisterVisitActivity_TAG: initObservers: ")
        viewModel.successOperation.observe(this) { success ->
            if (!success) {
                displayDialog(
                    isSuccess = false,
                    message = if (viewModel.isUpdate)
                        getString(R.string.txt_error_update_visit)
                    else
                        getString(R.string.txt_error_register_visit)
                ) { }
                return@observe
            }
            displayDialog(
                message = if (viewModel.isUpdate)
                    getString(R.string.txt_success_update)
                else
                    getString(R.string.txt_success_register)
            ) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    @SuppressLint("PrivateResource")
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
            AppCompatResources.getDrawable(this, R.drawable.ic_success)
        else
            AppCompatResources.getDrawable(this, R.drawable.ic_error)

        drawableIcon?.setTint(resources.getColor(R.color.md_theme_primary, null))

        showAlertDialog(
            context = this,
            title = title,
            message = message,
            hasCancelButton = false,
            isCancelable = false,
            icon = drawableIcon,
            onPositiveButtonClicked = {
                onAccept()
            }
        )
    }
}