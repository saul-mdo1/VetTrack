package com.example.vettrack.presentation.authentication.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.vettrack.R
import com.example.vettrack.databinding.LoginActivityLayoutBinding
import com.example.vettrack.presentation.home.MainActivity
import com.example.vettrack.presentation.authentication.signup.SignUpDialogFragment
import com.example.vettrack.presentation.authentication.signup.SignUpListener
import com.example.vettrack.presentation.utils.SIGNUP_DIALOG_TAG
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginActivity : AppCompatActivity(), SignUpListener {
    private lateinit var layout: LoginActivityLayoutBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("LoginActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        layout = DataBindingUtil.setContentView(this, R.layout.login_activity_layout)
        layout.lifecycleOwner = this
        layout.viewModel = viewModel

        initObservers()
        initViews()
    }

    private fun initObservers() {
        Timber.d("LoginActivity_TAG: initObservers: ")
        viewModel.successLogin.observe(this) { success ->
            if (success) {
                navigateHome()
            }
        }
    }

    private fun initViews() {
        Timber.d("LoginActivity_TAG: initViews: ")
        layout.tvSignUp.setOnClickListener {
            val dialog = SignUpDialogFragment()
            dialog.setListener(this)
            dialog.show(supportFragmentManager, SIGNUP_DIALOG_TAG)
        }
    }

    private fun navigateHome() {
        Timber.d("LoginActivity_TAG: navigateHome: ")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRegistered() {
        Timber.d("LoginActivity_TAG: onRegistered: ")
    }
}