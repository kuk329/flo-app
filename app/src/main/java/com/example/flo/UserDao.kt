package com.example.flo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {

    @Insert
    fun insert(user:User)

    @Query("SELECT * FROM UserTable")
    fun getUsers():List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password=:password")
    fun getUser(email:String,password:String):User? // 해당 정보의 유저가 없는것에 대비해 null 처리

}