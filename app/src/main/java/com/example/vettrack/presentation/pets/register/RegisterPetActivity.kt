package com.example.vettrack.presentation.pets.register

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.core.Session
import com.example.vettrack.databinding.RegisterPetActivityLayoutBinding
import com.example.vettrack.models.Gender
import com.example.vettrack.models.PetModel
import com.example.vettrack.presentation.utils.DATE_PICKER_DIALOG_TAG
import com.example.vettrack.presentation.utils.PET_TAG
import com.example.vettrack.presentation.utils.convertDateToMills
import com.example.vettrack.presentation.utils.convertMillisToDate
import com.example.vettrack.presentation.utils.showAlertDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
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
        getPet()

        viewModel.userId = Session.user?.uid ?: ""

        initViews()
    }

    @Suppress("DEPRECATION")
    private fun getPet() {
        Timber.d("RegisterPetActivity_TAG: getPetId: ")
        intent.extras?.getParcelable<PetModel>(PET_TAG)?.let { pet ->
            viewModel.documentId = pet.id
            viewModel.pet.postValue(pet)
        }
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

        viewModel.pet.observe(this) {
            viewModel.isUpdate = true
            viewModel.buttonText.postValue(R.string.txt_update)
            viewModel.screenTitle.postValue(R.string.txt_update_pet)

            viewModel.setPetData(it)

            val gender = Gender.fromId(it.gender)
            layout.actvGender.setText(gender.stringId)
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
        var dateMills: Long = MaterialDatePicker.todayInUtcMilliseconds()
        try {
            viewModel.birthdate.value?.let { date ->
                if (date.isNotBlank()) {
                    dateMills = convertDateToMills(date)
                }
            }
        } catch (e: Exception) {
            Timber.d("RegisterPetActivity_TAG: openDatePicker: ERROR set date: ${e.message} ")
        }

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