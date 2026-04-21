package com.vcco.hiltoncodechallenge.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vcco.hiltoncodechallenge.database.model.IPInfo

@Dao
interface IPInfoDao {
    @Insert
    fun InsertIpInfo(vararg ipInfo: IPInfo)

    @Query("Select * from IPInfo where `query` = :ip")
    fun getIpInfoFromIp(ip: String): IPInfo

    @Query("Select * from IPInfo")
    fun getAllIps(): List<IPInfo>

    @Update
    fun updateIpInfo(vararg ipInfo: IPInfo)
}