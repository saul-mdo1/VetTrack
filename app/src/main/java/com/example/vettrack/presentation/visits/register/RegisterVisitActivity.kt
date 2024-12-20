package com.example.vettrack.presentation.visits.register

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.RegisterVisitActivityLayoutBinding
import com.example.vettrack.presentation.utils.VISIT_ID_TAG
import com.example.vettrack.presentation.utils.convertDateToMills
import com.example.vettrack.presentation.utils.convertMillisToDate
import com.example.vettrack.presentation.utils.openDatePicker
import com.example.vettrack.presentation.utils.openTimePicker
import com.example.vettrack.presentation.utils.showAlertDialog
import com.google.android.material.datepicker.MaterialDatePicker
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
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        Timber.d("RegisterVisitActivity_TAG: showDatePicker: ")
        val dateTime: String? = if (viewModel.isUpdate) viewModel.date.value else null

        val dateMills: Long = try {
            dateTime?.let {
                if (it.isNotBlank()) {
                    val dateString = it.split(" ")[0]
                    convertDateToMills(dateString)
                } else
                    MaterialDatePicker.todayInUtcMilliseconds()
            } ?: MaterialDatePicker.todayInUtcMilliseconds()
        } catch (e: Exception) {
            Timber.d("RegisterVisitActivity_TAG: showDatePicker: ERROR set date: ${e.message} ")
            MaterialDatePicker.todayInUtcMilliseconds()
        }

        openDatePicker(
            dateMills = dateMills,
            futureConstraints = false,
            supportFragmentManager,
            onPositiveButtonClicked = { dateLong ->
                val date = convertMillisToDate(dateLong)
                showTimePicker(date)
            }
        )
    }

    private fun showTimePicker(date: String) {
        Timber.d("RegisterVisitActivity_TAG: showTimePicker: ")
        val dateTimeString: String? = if (viewModel.isUpdate) viewModel.date.value else null

        openTimePicker(
            defaultDate = dateTimeString,
            isFutureConstrained = false,
            supportFragmentManager = supportFragmentManager,
            onPositiveButtonClicked = { timeFormatted ->
                val dateTime = "$date $timeFormatted"
                viewModel.date.postValue(dateTime)
            },
            onNegativeButtonClicked = {
                viewModel.date.postValue(date)
            }
        )
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