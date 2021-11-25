package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey


// 회원가입을 위한 User Class
@Entity(tableName = "UserTable")
data class User(
    var email:String,
    var password : String
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}