package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.ServiceImageResponse
import com.chococard.carwash.repositories.HeaderRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServiceViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val serviceImage = MutableLiveData<ServiceImageResponse>()
    val getServiceImage: LiveData<ServiceImageResponse>
        get() = serviceImage

    private val imageService = MutableLiveData<ServiceImageResponse>()
    val getImageService: LiveData<ServiceImageResponse>
        get() = imageService

    fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: RequestBody
    ) = launchCallApi(
        request = { repository.callUploadImageService(file, statusService) },
        response = { serviceImage.value = it }
    )

    fun callFetchImageService() = launchCallApi(
        request = { repository.callFetchImageService() },
        response = { imageService.value = it }
    )

}