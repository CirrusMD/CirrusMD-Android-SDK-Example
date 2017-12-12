package com.cirrusmd.cirrusmdexample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Cory Clark on 12/8/17
 */

public class TokenRequest {
    @SerializedName("sdk_id")
    public String sdkId;

    @SerializedName("patient_id")
    public String patientId;
}
