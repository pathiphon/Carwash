package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.*
import okhttp3.MultipartBody

class HeaderRepository(
    private val api: HeaderAppService,
    private val db: AppDatabase
) : SafeApiRequest() {

    // user
    suspend fun callFetchUser() = apiRequest { api.callFetchUser() }
    suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    fun getUser() = db.getUserDao().getUser()
    suspend fun deleteUser() = db.getUserDao().deleteUser()

    suspend fun callUploadImageFile(file: MultipartBody.Part) =
        apiRequest { api.callUploadImageFile(file) }

    suspend fun callLogout() = apiRequest { api.callLogout() }

    suspend fun callChangeProfile(changePhone: ChangePhoneRequest) =
        apiRequest { api.callChangeProfile(changePhone) }

    suspend fun callChangePassword(changePassword: ChangePasswordRequest) =
        apiRequest { api.callChangePassword(changePassword) }

    suspend fun callSetLocation(setLocation: SetLocationRequest) =
        apiRequest { api.callSetLocation(setLocation) }

    suspend fun callFetchHistory(dateBegin: Long, dateEnd: Long) =
        apiRequest { api.callFetchHistory(dateBegin, dateEnd) }

    suspend fun callJobRequest() = apiRequest { api.callJobRequest() }

    // job
    suspend fun callJobResponse(jobAnswer: JobAnswerRequest) =
        apiRequest { api.callJobResponse(jobAnswer) }

    suspend fun saveJob(job: Job) = db.getJobDao().saveJob(job)
    fun getJob() = db.getJobDao().getJob()
    suspend fun deleteJob() = db.getJobDao().deleteJob()
    //job

    suspend fun callPayment(paymentStatus: Int) = apiRequest { api.callPayment(paymentStatus) }

    suspend fun callSetLogsActive(logsActive: LogsActiveRequest) =
        apiRequest { api.callSetLogsActive(logsActive) }

    suspend fun callSwitchSystem(switchSystem: SwitchSystemRequest) =
        apiRequest { api.callSwitchSystem(switchSystem) }

}
