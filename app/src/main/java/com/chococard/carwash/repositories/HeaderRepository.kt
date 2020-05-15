package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HeaderRepository(
    private val api: HeaderAppService,
    private val db: AppDatabase
) : SafeApiRequest() {

    // user
    suspend fun callFetchUser() = apiRequest { api.callFetchUser() }
    suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    fun getUser() = db.getUserDao().getUser()
    suspend fun deleteUser() = db.getUserDao().deleteUser()

    suspend fun callUploadImageFile(file: MultipartBody.Part, description: RequestBody) =
        apiRequest { api.callUploadImageFile(file, description) }

    suspend fun callLogout() = apiRequest { api.callLogout() }

    suspend fun callChangeProfile(name: String, identityCard: String, phone: String) =
        apiRequest { api.callChangeProfile(name, identityCard, phone) }

    suspend fun callChangePassword(oldPassword: String, newPassword: String) =
        apiRequest { api.callChangePassword(oldPassword, newPassword) }

    suspend fun callSetLocation(latitude: Double, longitude: Double) =
        apiRequest { api.callSetLocation(latitude, longitude) }

    suspend fun callFetchWallet(dateBegin: String, dateEnd: String) =
        apiRequest { api.callFetchWallet(dateBegin, dateEnd) }

    suspend fun callFetchHistory(dateBegin: String, dateEnd: String) =
        apiRequest { api.callFetchHistory(dateBegin, dateEnd) }

    // job
    suspend fun callJobRequest() = apiRequest { api.callJobRequest() }
    suspend fun saveJob(job: Job) = db.getJobDao().saveJob(job)
    fun getJob() = db.getJobDao().getJob()
    suspend fun deleteJob() = db.getJobDao().deleteJob()

    suspend fun callJobResponse(jobStatus: Int) = apiRequest { api.callJobResponse(jobStatus) }

    suspend fun callPayment(paymentStatus: Int) = apiRequest { api.callPayment(paymentStatus) }

    suspend fun callSetActiveState(activityState: Int) =
        apiRequest { api.callSetActiveState(activityState) }

    suspend fun callSetLogsActive(status: Int, keys: String) =
        apiRequest { api.callSetLogsActive(status, keys) }

}