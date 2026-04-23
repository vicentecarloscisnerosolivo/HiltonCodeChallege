package com.vcco.hiltoncodechallenge.ui.util

import com.vcco.hiltoncodechallenge.database.model.DomainInfo
import com.vcco.hiltoncodechallenge.model.ApiResponse
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

interface Mapper<I, O> {
    fun map(input: I): O
}

object IpSearchMapper : Mapper<ApiResponse, DomainInfo> {
    override fun map(input: ApiResponse): DomainInfo {
        return DomainInfo(
            input.query,
            input.status,
            input.country,
            input.countryCode,
            input.region,
            input.regionName,
            input.city,
            input.zipCode,
            input.latitude,
            input.longitude,
            input.timeZone,
            input.isp,
            input.organization,
            input.organizationNumber,
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        )
    }
}