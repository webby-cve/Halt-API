package com.example.studentidassignment2.di

import com.example.studentidassignment2.data.AppRepository
import com.example.studentidassignment2.data.Nit3213Api
import com.example.studentidassignment2.data.createNit3213Api
import com.example.studentidassignment2.ui.dashboard.DashboardViewModel
import com.example.studentidassignment2.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Nit3213Api> { createNit3213Api() }
    single { AppRepository(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}
