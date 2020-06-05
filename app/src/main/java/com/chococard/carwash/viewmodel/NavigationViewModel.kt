package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

class NavigationViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    val getDbJob = repository.getJob()

}
