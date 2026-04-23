package com.vcco.hiltoncodechallenge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vcco.hiltoncodechallenge.database.dao.DomainInfoDao
import com.vcco.hiltoncodechallenge.database.model.DomainInfo

@Database(entities = [DomainInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipInfoDao(): DomainInfoDao
}