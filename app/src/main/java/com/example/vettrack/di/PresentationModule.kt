package com.example.vettrack.di

import com.example.vettrack.presentation.authentication.login.LoginViewModel
import com.example.vettrack.presentation.authentication.signup.SignUpViewModel
import com.example.vettrack.presentation.home.MainViewModel
import com.example.vettrack.presentation.pets.list.PetsViewModel
import com.example.vettrack.presentation.scheduleVisits.register.ScheduleVisitViewModel
import com.example.vettrack.presentation.splash.SplashViewModel
import com.example.vettrack.presentation.visits.details.VisitDetailsViewModel
import com.example.vettrack.presentation.visits.list.VisitsViewModel
import com.example.vettrack.presentation.visits.completeVisit.CompleteVisitViewModel
import com.example.vettrack.presentation.visits.register.RegisterVisitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { RegisterVisitViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { VisitsViewModel(get()) }
    viewModel { VisitDetailsViewModel(get()) }
    viewModel { ScheduleVisitViewModel(get()) }
    viewModel { PetsViewModel(get()) }
    viewModel { CompleteVisitViewModel(get()) }
}

val presentationModule = listOf(
    viewModelsModule
)