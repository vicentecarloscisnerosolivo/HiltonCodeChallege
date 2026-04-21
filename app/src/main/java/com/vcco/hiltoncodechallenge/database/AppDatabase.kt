package com.vcco.hiltoncodechallenge.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vcco.hiltoncodechallenge.database.dao.IPInfoDao
import com.vcco.hiltoncodechallenge.database.model.IPInfo

@Database(entities = [IPInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipInfoDao(): IPInfoDao
}