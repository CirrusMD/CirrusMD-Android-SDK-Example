package com.cirrusmd.cirrusmdexample

import com.google.gson.annotations.SerializedName

data class TokenRequestKt(
    @SerializedName("sdk_id") var sdkId: String?,
    @SerializedName("patient_id") var patientId: String?
)
