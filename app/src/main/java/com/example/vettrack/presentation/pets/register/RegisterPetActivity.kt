package com.example.vettrack.presentation.pets.register

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.RegisterPetActivityLayoutBinding
import com.example.vettrack.models.Gender
import com.example.vettrack.presentation.utils.DATE_PICKER_DIALOG_TAG
import com.example.vettrack.presentation.utils.convertMillisToDate
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RegisterPetActivity : AppCompatActivity() {

    private lateinit var layout: RegisterPetActivityLayoutBinding
    private val viewModel by viewModel<RegisterPetViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("RegisterPetActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.register_pet_activity_layout)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        initObservers()
        initViews()

        viewModel.userId = Session.user?.uid ?: ""
    }

    private fun initObservers() {
        Timber.d("RegisterPetActivity_TAG: initObservers: ")
        viewModel.successOperation.observe(this) { success ->
            if (!success) {
                displayDialog(
                    isSuccess = false,
                    message = if (viewModel.isUpdate)
                        getString(R.string.txt_error_update_pet)
                    else
                        getString(R.string.txt_error_register_pet)
                ) { }
                return@observe
            }
            displayDialog(
                message = if (viewModel.isUpdate)
                    getString(R.string.txt_success_update_pet)
                else
                    getString(R.string.txt_success_register_pet)
            ) {
                setResult(Activity.RESULT_OK)
                finish()
            }

        }
    }

    private fun initViews() {
        Timber.d("RegisterPetActivity_TAG: initViews: ")
        initAutocompleteGender()

        layout.toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        layout.tilDate.setStartIconOnClickListener {
            openDatePicker()
        }
    }

    private fun initAutocompleteGender() {
        Timber.d("RegisterPetActivity_TAG: initAutocompleteGender: ")
        val genders = Gender.entries.map { getString(it.stringId) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        layout.actvGender.setAdapter(adapter)

        layout.actvGender.setOnItemClickListener { _, _, position, _ ->
            val selectedGenre = Gender.entries[position]
            viewModel.gender.postValue(selectedGenre.id)
        }
    }

    private fun openDatePicker() {
        Timber.d("RegisterPetActivity_TAG: openDatePicker: ")
        val dateMills: Long = MaterialDatePicker.todayInUtcMilliseconds()
        val constraints =
            CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .setSelection(dateMills)
            .build()

        datePicker.addOnPositiveButtonClickListener { dateLong ->
            val date = convertMillisToDate(dateLong)
            viewModel.birthdate.postValue(date)
        }

        datePicker.show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
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

        val dialog = MaterialAlertDialogBuilder(this)
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