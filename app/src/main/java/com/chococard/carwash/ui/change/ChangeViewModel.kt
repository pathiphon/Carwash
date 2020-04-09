package com.chococard.carwash.ui.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.ChangeProfileResponse
import com.chococard.carwash.data.repositories.ChangeRepository
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.BaseViewModel
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.NoInternetException
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class ChangeViewModel(private val repository: ChangeRepository) : BaseViewModel() {

    private val _upload = MutableLiveData<ResponseBody>()
    val upload: LiveData<ResponseBody>
        get() = _upload

    private val _changeProfile = MutableLiveData<ChangeProfileResponse>()
    val changeProfile: LiveData<ChangeProfileResponse>
        get() = _changeProfile

    fun uploadImageFile(file: MultipartBody.Part, description: RequestBody) {
        job = Coroutines.main {
            try {
                val response = repository.uploadImageFile(file, description)
                _upload.value = response
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
        }
    }

    fun changeProfile(name: String, identityCard: String, phone: String) {
        job = Coroutines.main {
            try {
                _changeProfile.value = repository.changeProfile(name, identityCard, phone)
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
        }
    }

    fun isIdentityCard(identityCard: String): Boolean {
        val ic = identityCard.substring(0, 12)
        var sumIdentityCard = 0
        ic.forEachIndexed { index, c ->
            sumIdentityCard += (13 - index) * c.toString().toInt()
        }
        val digit = (11 - (sumIdentityCard % 11)) % 10
        val realIdentityCard = ic + digit
        return realIdentityCard != identityCard
    }

    fun isTelephoneNumber(phone: String): Boolean {
        val tel = listOf("06", "08", "09")
        val p = phone.substring(0, 2)
        var isPhone = true
        tel.forEach {
            if (it == p) {
                isPhone = false
                return@forEach
            }
        }
        return isPhone
    }

}