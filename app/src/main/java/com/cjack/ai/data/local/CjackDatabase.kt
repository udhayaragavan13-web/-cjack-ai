package com.cjack.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cjack.ai.data.local.dao.CprSessionDao
import com.cjack.ai.data.local.dao.SosLogDao
import com.cjack.ai.data.local.entity.CprSessionEntity
import com.cjack.ai.data.local.entity.SosLogEntity

@Database(entities = [CprSessionEntity::class, SosLogEntity::class], version = 1, exportSchema = false)
abstract class CjackDatabase : RoomDatabase() {
    abstract val cprSessionDao: CprSessionDao
    abstract val sosLogDao: SosLogDao
}
