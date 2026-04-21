package com.vcco.hiltoncodechallenge.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IPInfo(
    @PrimaryKey val query: String,
    val status: String,
    val country: String,
    val countryCode: String,
    val region: String,
    val regionName: String,
    val city: String,
    val zipCode: String,
    val latitude: Double,
    val longitude: Double,
    val timeZone: String,
    val isp: String,
    val organization: String,
    val organizationNumber: String,
    val timestamp: String
)