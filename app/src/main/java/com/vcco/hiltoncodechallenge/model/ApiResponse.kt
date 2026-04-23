package com.vcco.hiltoncodechallenge.model

import android.R
import com.google.gson.annotations.SerializedName
import com.vcco.hiltoncodechallenge.model.utils.ModelConstants as Constants

data class ApiResponse(
    val status: String = "",
    val country: String = "",
    val countryCode: String = " ",
    val region: String = "",
    val regionName: String = "",
    val city: String = "",
    @SerializedName(Constants.zipCode)
    val zipCode: String = "",
    @SerializedName(Constants.latitude)
    val latitude: Double = 0.0,
    @SerializedName(Constants.longitude)
    val longitude: Double = 0.0,
    @SerializedName(Constants.timeZone)
    val timeZone: String = "",
    val isp: String = "",
    @SerializedName(Constants.organization)
    val organization: String = "",
    @SerializedName(Constants.organizationNumber)
    val organizationNumber: String = "",
    val query: String = "",
    val message: String? = null
)