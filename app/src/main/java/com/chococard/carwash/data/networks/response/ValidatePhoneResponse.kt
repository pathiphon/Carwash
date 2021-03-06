package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class ValidatePhoneResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.PHONE) val phoneNumber: String? = null
)
