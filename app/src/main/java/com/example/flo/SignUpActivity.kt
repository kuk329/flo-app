package com.example.flo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignUpBinding
import kotlin.math.sign

class SignUpActivity:AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener{
            signUp()
            finish()
        }



    }

    private fun getUser():User{
        val email :String = binding.signUpIdEt.text.toString()+"@"+binding.signUpEmailEt.text.toString()
        val password : String = binding.signUpPasswordEt.text.toString()

        return User(email,password)
    }

    private fun signUp(){
        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser()) // 사용자 정보 db에 추가
        Toast.makeText(this,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()


        val users = userDB.userDao().getUsers()
        Log.d("signup ",users.toString())
    }
}