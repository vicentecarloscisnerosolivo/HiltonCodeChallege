package com.vcco.hiltoncodechallenge.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vcco.hiltoncodechallenge.database.model.DomainInfo

@Dao
interface DomainInfoDao {
    @Insert
    fun InsertIpInfo(vararg ipInfo: DomainInfo)

    @Query("Select * from DomainInfo where `query` = :ip")
    fun getIpInfoFromIp(ip: String): DomainInfo?

    @Query("Select * from DomainInfo")
    fun getAllIps(): List<DomainInfo>

    @Update
    fun updateIpInfo(vararg ipInfo: DomainInfo)
}