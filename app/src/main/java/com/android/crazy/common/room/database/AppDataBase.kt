package com.android.crazy.common.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.crazy.common.room.converters.DateConverters
import com.android.crazy.common.room.dao.UserDao
import com.android.crazy.common.room.entity.User

/**
 * @author 刘贺贺
 */
@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}