package com.android.crazy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val age: Int = 0,
    val sex: Int = 0,
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val createTime: Date = Date(System.currentTimeMillis()),
    val updateTime: Date = Date(System.currentTimeMillis()),
    val createBy: String = "",
    val updateBy: String = ""
)

data class LoginForm(
    val email: String = "",
    val password: String = ""
)