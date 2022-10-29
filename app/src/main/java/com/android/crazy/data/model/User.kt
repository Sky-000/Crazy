package com.android.crazy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val age: Int,
    val sex: Int,
    val address: String,
    val phone: String,
    val email: String,
    val password: String,
    val createTime: Date,
    val updateTime: Date,
    val createBy: String,
    val updateBy: String
)